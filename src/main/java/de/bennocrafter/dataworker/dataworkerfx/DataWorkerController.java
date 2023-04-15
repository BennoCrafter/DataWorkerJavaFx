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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

import de.bennocrafter.dataworker.core.Entry;
import de.bennocrafter.dataworker.core.EntryBase;
import de.bennocrafter.dataworker.io.JSONDataWorkerReader;
import de.bennocrafter.dataworker.io.JSONDataWorkerWriter;

public class DataWorkerController implements Initializable {
	private static final String DATAWORKER_PROPERTIES = "dataworker.properties";
	private JSONDataWorkerWriter writer = new JSONDataWorkerWriter();
	private List<String> recentFiles = new ArrayList<>();

	private EntryBase selectedEntryBase;

	@FXML
	private TableView<Entry> tableView;

	@FXML
	private VBox recentBasesPane;

	@FXML
	private Label entryBaseNameLabel;

	@FXML
	private Button addEntryButton;

	@FXML
	private Button deleteEntryButton;
	private Entry selectedEntry;

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
					button.setWrapText(true);

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
		tableView.setEditable(true);
		tableView.getColumns().clear();
		tableView.getItems().clear();
		tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				this.selectedEntry = newSelection;
				deleteEntryButton.setDisable(false);
			} else {
				deleteEntryButton.setDisable(true);
				this.selectedEntry = null;
			}
		});
		EntryBase base = loadEntryBase(fileName);

		// define header of the table
		for (String attribute : base.getAttributes()) {
			TableColumn<Entry, String> column = new TableColumn<>(attribute);
			column.setCellValueFactory(new EntryPropertyValueFactory(attribute));
			column.setCellFactory(TextFieldTableCell.forTableColumn());
			column.setOnEditCommit(e -> {
				Entry entry = e.getTableView().getItems().get(e.getTablePosition().getRow());
				String newValue = e.getNewValue();
				if (newValue != null && !newValue.isEmpty()) {
					entry.addValueFor(attribute, newValue);
					saveCurrentEntryBase();
				} else {
					// Optional: Handle empty or null values as needed
				}
			});
			column.setEditable(true); // Enable cell editing
			tableView.getColumns().add(column);
		}


		// define content of the table
		for (Entry e: base.getEntries()) {
			tableView.getItems().add(e);
		}
		this.selectedEntryBase = base;
		if (base.getTableName() != null) this.entryBaseNameLabel.setText(base.getTableName());
		else this.entryBaseNameLabel.setText("Noname");
		this.addEntryButton.setDisable(false);
	}

	private void saveCurrentEntryBase() {
		String filename = this.selectedEntryBase.getLocation();
		writer.write(this.selectedEntryBase, filename);
	}

	@FXML
	void aboutMenuClicked(ActionEvent event) {
		new AboutMenu().show();
	}

	@FXML
	void onAdEntryButton(ActionEvent event) {
		// TODO
		System.out.println("Not implemented yet.");
	}

	@FXML
	void onDeleteEntryButton(ActionEvent event) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Bestätigung");
		alert.setHeaderText("Eintrag löschen");
		alert.setContentText("Soll der ausgewählte Eintrag wirklich gelöscht werden?");
		ButtonType okButton = new ButtonType("Löschen");
		ButtonType cancelButton = new ButtonType("Abbruch");
		alert.getButtonTypes().setAll(okButton, cancelButton);
		alert.showAndWait().ifPresent(response -> {
			if (response == okButton) {
				this.selectedEntryBase.remove(this.selectedEntry);
				tableView.getItems().remove(this.selectedEntry);
			}
		});
		saveCurrentEntryBase();
	}

}
