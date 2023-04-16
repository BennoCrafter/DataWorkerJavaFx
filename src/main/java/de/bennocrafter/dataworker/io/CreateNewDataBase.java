package de.bennocrafter.dataworker.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateNewDataBase {
    public void createFile(String fileName) throws IOException {
        // Create the JSON file
        String path = "DataBases/";
        File file = new File(path + fileName);
        if (file.createNewFile()) {
            System.out.println("JSON file created successfully: " + file.getAbsolutePath());

            // Write JSON template data to the file
            String jsonTemplate = "{\"entries\":[{}], \"data_id\":\"" + fileName.substring(0, fileName.lastIndexOf(".")) + "\",\"attributes\":[]}";
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonTemplate);
            fileWriter.close();
            System.out.println("JSON template data written to the file.");
        } else {
            System.out.println("Failed to create the JSON file: " + file.getAbsolutePath());
        }
    }
}
