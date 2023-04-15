package de.bennocrafter.dataworker.dataworkerfx;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

import de.bennocrafter.dataworker.core.Entry;
import de.bennocrafter.dataworker.core.EntryBase;
import de.bennocrafter.dataworker.io.JSONDataWorkerReader;

public class DataWorkerController implements Initializable {
	private static final String DATAWORKER_PROPERTIES = "dataworker.properties";
	private List<String> recentFiles = new ArrayList<>();

	private EntryBase selectedEntryBase;

	@FXML
	private TableView<Entry> entryBaseTable;

	@FXML
	private VBox recentBasesPane;

	@FXML
	private Label entryBaseNameLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updateRecentBasesPane();
	}

	private void updateRecentBasesPane() {
		Properties properties = new Properties();
		try (InputStream input = new FileInputStream(DATAWORKER_PROPERTIES)) {
			properties.load(input);
			String recentfiles = properties.getProperty("recentfiles");
			if (recentfiles != null) {
				this.recentFiles = Arrays.asList(recentfiles.split(","));
				for (String file: this.recentFiles) {
					String buttonLabel = loadEntryBase(file).getTableName();
					if (buttonLabel==null) buttonLabel = "Noname";

					Label button = new Label(buttonLabel);

					button.setOnMouseClicked(event -> {
						try {
							loadAndRefresh(file);
						}
						catch (Exception e) {
							throw new RuntimeException(e);
						}
					});

					recentBasesPane.getChildren().add(button);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}


	@FXML
	void onQuitAction(ActionEvent event) {
		Platform.exit();
	}

	private EntryBase loadEntryBase(String filename) {
		JSONDataWorkerReader r = new JSONDataWorkerReader();
		return r.read(filename);
	}

	private void loadAndRefresh(String fileName) throws Exception {
		entryBaseTable.getColumns().clear();
		entryBaseTable.getItems().clear();
		EntryBase base = loadEntryBase(fileName);

		// define header of the table
		for (String attribute: base.getAttributes()) {
			TableColumn<Entry, String> column = new TableColumn<>(attribute);
			column.setCellValueFactory(new EntryPropertyValueFactory(attribute));
			column.setCellFactory(TextFieldTableCell.forTableColumn());
			column.setOnEditCommit(e->e.getTableView().getItems().get(e.getTablePosition().getRow()).addValueFor(attribute, e.getNewValue()));
			entryBaseTable.getColumns().add(column);
		}

		// define content of the table
		for (Entry e: base.getEntries()) {
			entryBaseTable.getItems().add(e);
		}
		entryBaseTable.setEditable(true);
		this.selectedEntryBase = base;
		if (base.getTableName() != null) this.entryBaseNameLabel.setText(base.getTableName());
		else this.entryBaseNameLabel.setText("Noname");
	}
}
