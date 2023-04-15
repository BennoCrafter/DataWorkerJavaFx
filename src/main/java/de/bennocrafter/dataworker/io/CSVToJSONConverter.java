package de.bennocrafter.dataworker.io;

import de.bennocrafter.dataworker.core.EntryBase;


public class CSVToJSONConverter {
    public static void main(String[] args) throws Exception {
        // reads a csv file and converts it to a json file

        CSVDataWorkerReader reader = new CSVDataWorkerReader();
        JSONDataWorkerWriter jsonWriter = new JSONDataWorkerWriter();

        String fileName = "example a. more.csv";
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        EntryBase base = reader.read(fileName, name);
        jsonWriter.write(base, name);
    }
}
