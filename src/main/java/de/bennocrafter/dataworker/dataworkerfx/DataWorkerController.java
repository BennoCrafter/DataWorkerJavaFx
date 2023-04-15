package de.bennocrafter.dataworker.dataworkerfx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DataWorkerController {
	@FXML
	private Label welcomeText;

	@FXML
	protected void onHelloButtonClick() {
		welcomeText.setText("Welcome to JavaFX Application!");
	}
}