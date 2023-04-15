package de.bennocrafter.dataworker.io;

import de.bennocrafter.dataworker.core.EntryBase;

public class Converter {
    public static void main(String[] args) throws Exception {
        String outputName = "Weinbuecher.json";
        CSVDataWorkerReader reader = new CSVDataWorkerReader();
        EntryBase base = reader.read("Weinbuecher.csv", "Weinbücher");

        JSONDataWorkerWriter jsonWriter = new JSONDataWorkerWriter();
        jsonWriter.write(base, outputName);


        String inputName = "Weinbuecher.json";
        JSONDataWorkerReader jsonReader = new JSONDataWorkerReader();
        EntryBase basey = jsonReader.read(inputName);
    }
}
