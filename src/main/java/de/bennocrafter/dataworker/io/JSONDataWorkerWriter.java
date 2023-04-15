package de.bennocrafter.dataworker.io;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import de.bennocrafter.dataworker.core.Entry;
import de.bennocrafter.dataworker.core.EntryBase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONDataWorkerWriter {
    private String dataId;
    private List<String> attributes;

    private JSONObject writeData(EntryBase base){
        // Create JSON object
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("data_id", base.getTableName());
            jsonObject.put("attributes", new JSONArray(base.getAttributes()));
            JSONArray entriesArray = new JSONArray();
            for (Entry e : base.getEntries()) {
                JSONObject entryJson = new JSONObject();
                for (String attribute : base.getAttributes()) {
                    String value = e.valueFor(attribute);
                    entryJson.put(attribute, value);
                }
                entriesArray.put(entryJson);
            }
            jsonObject.put("entries", entriesArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void write(EntryBase base, String outputName) {
        JSONObject jsonObject = writeData(base);
        writeToFile(outputName, jsonObject);
    }

    private void writeToFile(String outputName, JSONObject jsonObject) {
        try (FileWriter fileWriter = new FileWriter(outputName)) {
            fileWriter.write(jsonObject.toString(4)); // 4 is the indentation level
            System.out.println("JSON data has been saved to file: data.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
