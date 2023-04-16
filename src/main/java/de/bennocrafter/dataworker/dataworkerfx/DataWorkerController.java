package de.bennocrafter.dataworker.dataworkerfx;

import java.io.*;
import java.net.URL;
import java.util.*;

import de.bennocrafter.dataworker.io.CreateNewDataBase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

import de.bennocrafter.dataworker.core.Entry;
import de.bennocrafter.dataworker.core.EntryBase;
import de.bennocrafter.dataworker.io.JSONDataWorkerReader;
import de.bennocrafter.dataworker.io.JSONDataWorkerWriter;
import javafx.stage.Stage;

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

	@FXML
	private TextField searchBar;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updateRecentBasesPane();
	}

	private void updateRecentBasesPane() {
		recentBasesPane.getChildren().clear();

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

	private void addToRecentFiles(String newFile) {
		// Load existing properties file
		Properties properties = new Properties();
		try (InputStream inputStream = new FileInputStream("dataworker.properties")) {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Get current recentfiles value
		String currentRecentFiles = properties.getProperty("recentfiles");

		// Add newFile to recentfiles value
		currentRecentFiles = newFile + "," + currentRecentFiles;

		// Update recentfiles value in properties object
		properties.setProperty("recentfiles", currentRecentFiles);

		// Write updated properties back to file
		try (OutputStream outputStream = new FileOutputStream("dataworker.properties")) {
			properties.store(outputStream, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@FXML
	void onQuitAction(ActionEvent event) {
		Platform.exit();
	}
	@FXML
	void onUndoAction(ActionEvent event){
		System.out.println("undo");
	}
	@FXML
	void onloadBackupAction(ActionEvent event){
		System.out.println("load backup");
	}
	@FXML
	void onSaveAction(ActionEvent event){
		saveCurrentEntryBase();
	}
	@FXML
	void onNewDataBaseAction(ActionEvent event) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Neue DatenBank");
		dialog.setHeaderText(null);
		//dialog.setContentText("Enter a name for the new database:");
		dialog.setContentText("Gebe einen Namen für die neue DatenBank ein:");
		Optional<String> result = dialog.showAndWait();
		result.ifPresent(databaseName -> {
			String newFilename = "DataBases/" + databaseName + ".json";
			CreateNewDataBase cndb = new CreateNewDataBase();
			try {
				cndb.createFile(databaseName + ".json");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			try {
				addToRecentFiles(newFilename);
				loadAndRefresh(newFilename);
				updateRecentBasesPane();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}


	@FXML
	void onReloadAction(ActionEvent event) {
		// TODO
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
		if (selectedEntryBase != null) {
			Entry newEntry = new Entry();
			for (String attribute : selectedEntryBase.getAttributes()) {
				newEntry.addValueFor(attribute, ""); // Set default value as empty string
			}
			selectedEntryBase.add(newEntry);
			tableView.getItems().add(newEntry);
			saveCurrentEntryBase();

		}

	}

	@FXML
	void onSearchPromtEntered(ActionEvent event){

		System.out.println(searchBar.getText());
		System.out.println(selectedEntryBase.allMatches(searchBar.getText()));
		searchBar.setOnMouseClicked(e -> searchBar.selectAll());

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
