package de.bennocrafter.dataworker.dataworkerfx;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import de.bennocrafter.dataworker.core.Entry;
import de.bennocrafter.dataworker.core.EntryBase;
import de.bennocrafter.dataworker.core.LayoutInfo;

public class SingleEdit implements Initializable {
	public static final String LAYOUT_KEY_ROWSIZE = "rowsize";
	@FXML
	private Button singleEditCloseButton;
	@FXML
	private GridPane gridPane;
	/*
	@FXML
	private BorderPane borderPane;
	@FXML
	private Button nextButton;
	@FXML
	private Button previousButton;
	Ü/
	 */
	@FXML
	private ScrollPane scrollPane;

	private List<TextArea> inputFields;

	public void show() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("dataworker-editScene.fxml"));
			Parent root = loader.load();
			//root.setStyle("-fx-font-size: 24;");
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
		scrollPane.setContent(gridPane);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20));

		gridPane.getColumnConstraints().clear();
		ColumnConstraints columnConstraints1 = new ColumnConstraints();
		columnConstraints1.setPercentWidth(20);
		gridPane.getColumnConstraints().add(columnConstraints1);
		ColumnConstraints columnConstraints2 = new ColumnConstraints();
		columnConstraints2.setPercentWidth(80);
		gridPane.getColumnConstraints().add(columnConstraints2);

		reFreshForEnrty();
	}

	private void reFreshForEnrty() {
		initializeInputFields();
		initTabListenerForInputFields();
	}

	private void initTabListenerForInputFields() {
		List<String> attributes = DatabaseSingleton.getInstance().getEntryBase().getAttributes();
		int row = 0;
		for (String attribute : attributes) {
			gridPane.add(new Label(attribute), 0, row);
			TextArea valueInput = this.inputFields.get(row);

			int nextPosition = row + 1;
			if (nextPosition > attributes.size()-1) {
				nextPosition = 0;
			}
			TextArea nextInputValue = this.inputFields.get(nextPosition);
			int prevPosition = row - 1;
			if (prevPosition < 0) {
				prevPosition = attributes.size()-1;
			}
			TextArea prevInputValue = this.inputFields.get(prevPosition);
			// overwrite the Tab key so we directly jump to the next/prev input field
			valueInput.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
					prevInputValue.requestFocus();
					event.consume(); // Consume the event to prevent the default behavior
				}
				else if (event.getCode() == KeyCode.TAB) {
					nextInputValue.requestFocus();
					event.consume(); // Consume the event to prevent the default behavior
				}
			});
			gridPane.add(valueInput, 1, row);
			row++;
		}
	}

	private void initializeInputFields() {
		EntryBase entryBase = DatabaseSingleton.getInstance().getEntryBase();
		Entry entry = DatabaseSingleton.getInstance().getEntry();

		inputFields = new ArrayList<>(entryBase.getAttributes().size());
		for (String attribute: entryBase.getAttributes()) {
			TextArea valueInput = new TextArea(entry.valueFor(attribute));
			valueInput.setPrefWidth(600);
			double fontSize = valueInput.getFont().getSize();
			double lineHeight = fontSize * 1.5;
			int linesToShow = getLinesOfField(attribute);
			valueInput.setPrefHeight(lineHeight * linesToShow + 10);
			valueInput.setWrapText(true);

			// We need to save the database when the text has changed
			valueInput.focusedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if (!newValue) { // If focus is lost (newValue is false)
						if (!valueInput.getText().equals(entry.valueFor(attribute))) {
							Entry entry = DatabaseSingleton.getInstance().getEntry();
							entry.addValueFor(attribute, valueInput.getText());
							DatabaseSingleton.getInstance().saveEntryBase();
						}
					}
				}
			});
			inputFields.add(valueInput);
		}
	}

	private int getLinesOfField(String attribute) {
		EntryBase entryBase = DatabaseSingleton.getInstance().getEntryBase();
		if (entryBase.hasLayout(attribute)) {
			LayoutInfo layout = entryBase.getLayout(attribute);
			String rowsize = layout.getInfo(LAYOUT_KEY_ROWSIZE);
			try {
				return Integer.parseInt(rowsize);
			} catch (NumberFormatException e) {
				return 1;
			}
		} else {
			return 1;
		}
	}

	@FXML
	void onNextButton(ActionEvent event) {
		DatabaseSingleton.getInstance().getEntry();
		Entry entry = DatabaseSingleton.getInstance().getEntryBase().nextEntryOf(DatabaseSingleton.getInstance().getEntry());
		DatabaseSingleton.getInstance().setEntry(entry);
		reFreshForEnrty();
	}

	@FXML
	void onPreviousButton(ActionEvent event) {
		DatabaseSingleton.getInstance().getEntry();
		Entry entry = DatabaseSingleton.getInstance().getEntryBase().previousEntryOf(DatabaseSingleton.getInstance().getEntry());
		DatabaseSingleton.getInstance().setEntry(entry);
		reFreshForEnrty();
	}
}
