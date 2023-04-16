package de.bennocrafter.dataworker.dataworkerfx;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import de.bennocrafter.dataworker.core.Entry;
import de.bennocrafter.dataworker.core.EntryBase;

public class SingleEdit {
	@FXML
	private Button singleEditCloseButton;
	@FXML
	private GridPane gridPane;

	public void show(EntryBase entryBase, Entry entry) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("dataworker-editScene.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Edit");
			Scene scene = new Scene(root, 800, 600);
			stage.setScene(scene);
			createFormPane(entry, entryBase);

			stage.initModality(Modality.APPLICATION_MODAL);

			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createFormPane(Entry entry, EntryBase entryBase) {
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20));

		int row = 0;
		for (String attribute : entryBase.getAttributes()) {
			gridPane.add(new Label(attribute), row, 0);
			gridPane.add(new TextField(entry.valueFor(attribute)), row, 1);
			row++;
		}
	}

	@FXML
	void onSingleEditClose(ActionEvent event) {
		((Stage) singleEditCloseButton.getScene().getWindow()).close();
	}
	public void initialize(){
	}
}
