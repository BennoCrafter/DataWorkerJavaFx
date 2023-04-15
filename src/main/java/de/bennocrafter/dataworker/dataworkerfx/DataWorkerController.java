package de.bennocrafter.dataworker.dataworkerfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;

import de.bennocrafter.dataworker.core.Entry;

public class DataWorkerController {

	@FXML
	private TableView<Entry> entryBaseTable;

	@FXML
	private Label welcomeText;

	@FXML
	void onContextEntryBaseTable(ContextMenuEvent event) {
	}

	@FXML
	void onHelloButtonClick(ActionEvent event) {
		TableColumn<Entry, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<Entry, Integer> ageColumn = new TableColumn<>("Age");
		ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

		entryBaseTable.getColumns().add(nameColumn);
		entryBaseTable.getColumns().add(ageColumn);
		Entry textEntry = new Entry();
		textEntry.addValueFor("name", "joba").addValueFor("age", "50");
		entryBaseTable.getItems().add(textEntry);

	}

	@FXML
	void onSortTable(ActionEvent event) {

	}

}
