module de.bennocrafter.dataworker.dataworkerfx {
	requires javafx.controls;
	requires javafx.fxml;

	requires org.controlsfx.controls;
	requires com.dlsc.formsfx;
	requires com.almasb.fxgl.all;
	requires com.opencsv;
    requires org.json;

    opens de.bennocrafter.dataworker.dataworkerfx to javafx.fxml;
	exports de.bennocrafter.dataworker.dataworkerfx;
}