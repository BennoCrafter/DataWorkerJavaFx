package de.bennocrafter.dataworker.io;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import de.bennocrafter.dataworker.core.Entry;
import de.bennocrafter.dataworker.core.EntryBase;

public class JSONDataWorkerReader {
    public EntryBase read(String inputName) {
        EntryBase base = new EntryBase();
        try (FileReader fileReader = new FileReader(inputName)) {
            JSONTokener jsonTokener = new JSONTokener(fileReader);
            JSONObject loadedJsonObject = new JSONObject(jsonTokener);

            // Extract data from loaded JSON object
            String loadedDataId = loadedJsonObject.getString("data_id");
            base.setName(loadedDataId);

            JSONArray loadedAttributesArray = loadedJsonObject.getJSONArray("attributes");
            List<String> loadedAttributes = new ArrayList<>();
            for (int i = 0; i < loadedAttributesArray.length(); i++) {
                String attribute = loadedAttributesArray.getString(i);
                loadedAttributes.add(attribute);
            }
            base.setAttributes(loadedAttributes);

            JSONArray loadedEntriesArray = loadedJsonObject.getJSONArray("entries");
            List<Map<String, String>> loadedEntries = new ArrayList<>();
            for (int i = 0; i < loadedEntriesArray.length(); i++) {
                JSONObject entryObject = loadedEntriesArray.getJSONObject(i);
                Entry e = new Entry();
                for (String attr: loadedAttributes) {
                    e.addValueFor(attr, entryObject.getString(attr));
                }
                base.add(e);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return base;
    }
}
