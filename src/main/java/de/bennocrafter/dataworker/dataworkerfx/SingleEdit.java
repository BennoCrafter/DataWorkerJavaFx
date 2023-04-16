package de.bennocrafter.dataworker.dataworkerfx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import de.bennocrafter.dataworker.core.Entry;
import de.bennocrafter.dataworker.core.EntryBase;

public class SingleEdit implements Initializable {
	@FXML
	private Button singleEditCloseButton;
	@FXML
	private GridPane gridPane;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private BorderPane borderPane;

	public void show() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("dataworker-editScene.fxml"));
			Parent root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Edit");
			Scene scene = new Scene(root, 800, 600);
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void onSingleEditClose(ActionEvent event) {
		((Stage) singleEditCloseButton.getScene().getWindow()).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20));

		EntryBase entryBase = DatabaseSingleton.getInstance().getEntryBase();
		Entry entry = DatabaseSingleton.getInstance().getEntry();

		ColumnConstraints columnConstraints1 = new ColumnConstraints();
		columnConstraints1.setPercentWidth(20);
		gridPane.getColumnConstraints().add(columnConstraints1);
		ColumnConstraints columnConstraints2 = new ColumnConstraints();
		columnConstraints2.setPercentWidth(80);
		gridPane.getColumnConstraints().add(columnConstraints2);

		int row = 0;
		for (String attribute : entryBase.getAttributes()) {
			gridPane.add(new Label(attribute), 0, row);
			gridPane.add(new TextField(entry.valueFor(attribute)), 1, row);
			row++;
		}
	}
}
