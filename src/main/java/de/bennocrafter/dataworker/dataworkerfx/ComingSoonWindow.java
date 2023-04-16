package de.bennocrafter.dataworker.dataworkerfx;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ComingSoonWindow {

    void showAlert(){
        showAlert("sfdf");
    }
    void showAlert(String feature){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Coming Soon");
        alert.setHeaderText("Das Feature " + feature + " kommt bald!");
        alert.setContentText("Dieses Feature ist noch nicht implementiert.");
        ButtonType okButton = new ButtonType("Ok");
        alert.getButtonTypes().setAll(okButton);
        alert.showAndWait().ifPresent(response -> {
        });
    }

}