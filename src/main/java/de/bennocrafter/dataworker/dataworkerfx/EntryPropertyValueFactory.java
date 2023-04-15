package de.bennocrafter.dataworker.dataworkerfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import de.bennocrafter.dataworker.core.Entry;

public class EntryPropertyValueFactory<S, T> extends PropertyValueFactory {
	public EntryPropertyValueFactory(String s) {
		super(s);
	}

	@Override
	public ObservableValue call(TableColumn.CellDataFeatures cellDataFeatures) {
		String s = ((Entry)(cellDataFeatures.getValue())).valueFor(this.getProperty());
		StringProperty sp = new SimpleStringProperty(s);
		return  sp;
	}
}
