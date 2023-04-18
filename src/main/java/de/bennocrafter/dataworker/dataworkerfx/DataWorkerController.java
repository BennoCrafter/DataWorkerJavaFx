package de.bennocrafter.dataworker.dataworkerfx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import de.bennocrafter.dataworker.core.Entry;
import de.bennocrafter.dataworker.core.EntryBase;
import de.bennocrafter.dataworker.io.BackupZipping;
import de.bennocrafter.dataworker.io.CreateNewDataBase;
import de.bennocrafter.dataworker.io.JSONDataWorkerReader;
import de.bennocrafter.dataworker.io.JSONDataWorkerWriter;

public class DataWorkerController implements Initializable {
	public static final String DATAWORKER_PROPERTIES = "dataworker.properties";
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

	@FXML
	private Button singleEditButton;


	@FXML
	void onSingleEditButton(ActionEvent event) {
		SingleEdit singleEdit = new SingleEdit();
		DatabaseSingleton.getInstance().setEntry(selectedEntry);
		DatabaseSingleton.getInstance().setEntryBase(selectedEntryBase);
		singleEdit.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		BackupZipping backupApp = new BackupZipping();
		backupApp.start("DataBases", "DataBases/Backups", "recents");
		backupApp.reduce(20);
		updateRecentBasesPane();
	}

	private void updateRecentBasesPane() {
		recentBasesPane.getChildren().clear();
		Properties properties = new Properties();
		Path propertyFile = Paths.get(DATAWORKER_PROPERTIES);
		try (BufferedReader reader = Files.newBufferedReader(propertyFile, StandardCharsets.UTF_8)) {
			properties.load(reader);
			String recentfiles = properties.getProperty("recentfiles");
			if (recentfiles != null) {
				this.recentFiles = Arrays.asList(recentfiles.split(","));
				for (String file: this.recentFiles) {
					String buttonLabel = loadEntryBase(file).getTableName();
					if (buttonLabel==null) buttonLabel = "Noname";
					Label button = new Label(buttonLabel);
					button.setWrapText(true);

					Tooltip tooltip = new Tooltip(file); // Create a tooltip with the file name
					button.setTooltip(tooltip); // Set tooltip for the label/button

					button.setOnMouseClicked(event -> {
						try {
							loadAndRefresh(file);
						}
						catch (Exception e) {
							throw new RuntimeException(e);
						}
					});

					// Add mouse event handlers for hover effect
					button.setOnMouseEntered(event -> {
						button.setStyle("-fx-background-color: #e0e0e0;"); // Set the background color to a lighter shade
					});
					button.setOnMouseExited(event -> {
						button.setStyle("-fx-background-color: transparent;"); // Reset the background color
					});

					recentBasesPane.getChildren().add(button);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void addToRecentFiles(String newFile) {
		Path propertyFile = Paths.get(DATAWORKER_PROPERTIES);
		Properties properties = new Properties();
		try (BufferedReader reader = Files.newBufferedReader(propertyFile, StandardCharsets.UTF_8)) {
			properties.load(reader);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String currentRecentFiles = properties.getProperty("recentfiles");
		currentRecentFiles = newFile + "," + currentRecentFiles;
		properties.setProperty("recentfiles", currentRecentFiles);
		try (BufferedWriter writer = Files.newBufferedWriter(propertyFile, StandardCharsets.UTF_8)) {
			properties.store(writer, null);
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
		new ComingSoonWindow().showAlert("undo");
	}
	@FXML
	void onloadBackupAction(ActionEvent event){
		new ComingSoonWindow().showAlert("load backup");
	}
	@FXML
	void onSaveAction(ActionEvent event){
		saveCurrentEntryBase();
	}
	@FXML
	void onLoadEntryBaseAction(ActionEvent event){
		String destinationFolderPath = "DataBases/";
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select a File");

		// Set initial directory (optional)
		File initialDirectory = new File(System.getProperty("user.home"));
		fileChooser.setInitialDirectory(initialDirectory);

		// Show the FileChooser and get the selected file
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			System.out.println("Selected file: " + selectedFile.getAbsolutePath());
			try {
				// Create destination path with filename
				String destinationFilePath = destinationFolderPath + selectedFile.getName();
				Files.copy(selectedFile.toPath(), Paths.get(destinationFilePath));
				System.out.println("File copy completed successfully.");
				addToRecentFiles("DataBases/" + selectedFile.getName());
				loadAndRefresh("DataBases/" + selectedFile.getName());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			System.out.println("No file selected.");
		}
	}

	@FXML
	void onNewDataBaseAction(ActionEvent event) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Neue Datenbank");
		dialog.setHeaderText(null);
		//dialog.setContentText("Enter a name for the new database:");
		dialog.setContentText("Gebe einen Namen für die neue Datenbank ein:");
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
		//new ComingSoonWindow().showAlert("reloading");
	}

	private EntryBase loadEntryBase(String filename) {
		JSONDataWorkerReader r = new JSONDataWorkerReader();
		return r.read(filename);
	}

	private void loadAndRefresh(String fileName) throws Exception {
		tableView.setEditable(true);
		tableView.getColumns().clear();
		tableView.getItems().clear();
		TableView.TableViewFocusModel<Entry> focusModel = tableView.getFocusModel();
		tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				this.selectedEntry = newSelection;
				deleteEntryButton.setDisable(false);
				singleEditButton.setDisable(false);
			} else {
				deleteEntryButton.setDisable(true);
				singleEditButton.setDisable(true);
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
