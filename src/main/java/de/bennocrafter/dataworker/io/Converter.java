package de.bennocrafter.dataworker.io;

import de.bennocrafter.dataworker.core.EntryBase;

public class Converter {
    public static void main(String[] args) throws Exception {
        String outputName = "Weinbuecher.json";
        CSVDataWorkerReader reader = new CSVDataWorkerReader();
        EntryBase base = reader.read("Weinbuecher.csv", "Weinbücher");

        JSONWriter jsonWriter = new JSONWriter();
        jsonWriter.write(base, outputName);

    }
}
