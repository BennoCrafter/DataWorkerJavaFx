package de.bennocrafter.dataworker.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.bennocrafter.dataworker.core.Entry;
import de.bennocrafter.dataworker.core.EntryBase;
import de.bennocrafter.dataworker.core.LayoutInfo;

public class JSONDataWorkerWriter {
    private String dataId;
    private List<String> attributes;

    private JSONObject writeData(EntryBase base){
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

            JSONArray layoutInfo = createLayoutInfo(base);
            jsonObject.put("layout", layoutInfo);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONArray createLayoutInfo(EntryBase base) {
        JSONArray array = new JSONArray();
        for (String attribute : base.getAttributes()) {
            if (base.hasLayout(attribute)) {
                JSONObject layoutObject = new JSONObject();
                LayoutInfo layout = base.getLayout(attribute);
                for (String key : layout.getInfoKeys()) {
                    layoutObject.put(key, layout.getInfo(key));
                }
                array.put(layoutObject);
            }
        }
        return array;
    }

    public void write(EntryBase base, String outputName) {
        JSONObject jsonObject = writeData(base);
        writeToFile(outputName, jsonObject);
    }

    private void writeToFile(String outputName, JSONObject jsonObject) {
        try (FileWriter fileWriter = new FileWriter(outputName)) {
            fileWriter.write(jsonObject.toString(3)); // 4 is the indentation level
            String currentPath = new java.io.File(outputName).getCanonicalPath();
            System.out.println("JSON data has been saved to file: " + currentPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
