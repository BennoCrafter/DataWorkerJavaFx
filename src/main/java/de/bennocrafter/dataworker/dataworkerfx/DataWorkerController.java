package de.bennocrafter.dataworker.dataworkerfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.ContextMenuEvent;

import de.bennocrafter.dataworker.core.Entry;
import de.bennocrafter.dataworker.core.EntryBase;
import de.bennocrafter.dataworker.io.CSVDataWorkerReader;

public class DataWorkerController {

	@FXML
	private TableView<Entry> entryBaseTable;

	@FXML
	private Label welcomeText;

	@FXML
	void onContextEntryBaseTable(ContextMenuEvent event) {
	}

	@FXML
	void onHelloButtonClick(ActionEvent event) throws Exception {
		String fileName = "Weinbuecher.csv";
		CSVDataWorkerReader r = new CSVDataWorkerReader();
		EntryBase base = r.read(fileName, "Weinbuecher");

		// define header of the table
		for (String attribute: base.getAttributes()) {
			TableColumn<Entry, String> column = new TableColumn<>(attribute);
			column.setCellValueFactory(new EntryPropertyValueFactory(attribute));
			entryBaseTable.getColumns().add(column);
		}

		// define content of the table
		for (Entry e: base.getEntries()) {
			entryBaseTable.getItems().add(e);
		}
	}

	@FXML
	void onSortTable(ActionEvent event) {

	}

}
