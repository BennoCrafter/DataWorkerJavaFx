package de.bennocrafter.dataworker.dataworkerfx;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class AboutMenu {
	@FXML
	Button closeButton;
	@FXML
	Label versionNum;
	@FXML
	Label versionType;

	HashMap<String, String> version = new HashMap<String, String>(){{put("number", "0.1"); put("type", "alpha");}};
	public void show() {
		try {
			// Load the FXML file
			FXMLLoader loader = new FXMLLoader(getClass().getResource("dataworker-aboutMenu.fxml"));
			Parent root = loader.load();
 			Stage stage = new Stage();
			// Create a new Stage
			stage.setTitle("About");

			// Set the loaded FXML file as the root of the Scene
			Scene scene = new Scene(root, 400, 300);
			stage.setScene(scene);

			// Set the Modality of the Stage to APPLICATION_MODAL
			stage.initModality(Modality.APPLICATION_MODAL);

			// Show the Stage and wait for it to be closed
			stage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@FXML
	void onAboutExitButtonClicked(ActionEvent event) {
		// if exit button is pressed
		}


	public void initialize(){
		versionNum.setText(version.get("number"));
		versionType.setText(version.get("type"));
		closeButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				((Stage) closeButton.getScene().getWindow()).close();
			}
		});
	}

}
