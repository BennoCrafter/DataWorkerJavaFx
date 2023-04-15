package de.bennocrafter.dataworker.io;

import de.bennocrafter.dataworker.core.EntryBase;

import java.util.Arrays;
import java.util.List;

public class CSVToJSONConverter {
    public static void main(String[] args) throws Exception {
        String folder_path = "/Users/benno/Downloads/Bento_Exporte/";
        List<String> outputNames = Arrays.asList("DataBases/Kataloge.json", "DataBases/Frankonia u.Reise.json", "Komische Bücher.csv", "DataBases/Erotika.json", "Reisebücher.csv", "Weinbücher.csv", "DataBases/Zinnfiguren.json", "Erotische Bildbände.csv", "Zinnfiguren Bücher.csv", "Kochbücher.csv");

        CSVDataWorkerReader reader = new CSVDataWorkerReader();
        for (String currentOutputName: outputNames) {
            EntryBase base = reader.read(folder_path+currentOutputName, currentOutputName.split("\\.")[0]);
            JSONDataWorkerWriter jsonWriter = new JSONDataWorkerWriter();
            jsonWriter.write(base, currentOutputName);
        }

    }
}
