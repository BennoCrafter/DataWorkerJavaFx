package de.bennocrafter.dataworker.io;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.*;

//import static de.bennocrafter.dataworker.io.ReadProperties.DATAWORKER_PROPERTIES;


public class BackupZipping {
    public static final String DATAWORKER_PROPERTIES = "dataworker.properties";
    private List<String> recentFiles = new ArrayList<>();
    private String zipFilePath;
    private String sourceFolderPath;
    private String outputFolderPath;
    private String zipFileName;


    public static void main(String[] args) {
        BackupZipping backupApp = new BackupZipping();
        backupApp.start("DataBases", "DataBases/Backups", "recents");
        backupApp.reduce(2);
    }

    public void reduce(int maxNumberOfBackups) {
        if (outputFolderPath == null) {
            System.err.println("Backup directory is not set.");
            return;
        }

        File[] backupFiles = new File(outputFolderPath).listFiles();
        if (backupFiles != null && backupFiles.length > maxNumberOfBackups) {
            // Sort backup files by last modified time in ascending order
            java.util.Arrays.sort(backupFiles, Comparator.comparingLong(File::lastModified));

            int numBackupsToDelete = backupFiles.length - maxNumberOfBackups;
            for (int i = 0; i < numBackupsToDelete; i++) {
                try {
                    Files.deleteIfExists(backupFiles[i].toPath()); // Delete oldest backup file
                } catch (IOException e) {
                    System.err.println("Failed to delete backup file: " + backupFiles[i].getName());
                    e.printStackTrace();
                }
            }

            System.out.println("Reduced backups to max limit of " + maxNumberOfBackups);
        } else {
            System.out.println("Number of backups is within the limit.");
        }
    }

    private void zipFolder(String sourceFolderPath, ZipOutputStream zos, String parentFolder) throws IOException {
        File folder = new File(sourceFolderPath);
        for (File file : folder.listFiles()) {
            if (file.isFile()) {
                FileInputStream fis = new FileInputStream(file);
                if(recentFiles.contains("DataBases/" + file.getName())){
                    zos.putNextEntry(new ZipEntry(parentFolder + file.getName()));

                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }

                    zos.closeEntry();
                }
                fis.close();
            }
        }
    }

    private void loadRecentFiles() {
        Properties properties = new Properties();
        Path propertyFile = Paths.get(DATAWORKER_PROPERTIES);
        try (BufferedReader reader = Files.newBufferedReader(propertyFile, StandardCharsets.UTF_8)) {
            properties.load(reader);
            String recentFilesString = properties.getProperty("recentfiles");
            if (recentFilesString != null) {
                recentFiles = Arrays.asList(recentFilesString.split(","));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private String generateZipFileName () {
        // Generate timestamp in format yyyyMMddHHmm
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String timestamp = sdf.format(new Date());
        return timestamp + "_database_backup.zip"; // Customize the zip file name format as needed
    }

    public void start(String sourceFolder, String outputFolder, String whichFiles){
        String sourceFolderPath = sourceFolder; // Path to the folder you want to zip
        outputFolderPath = outputFolder; // Path to the output folder
        String filesToZip = whichFiles;
        String zipFileName = generateZipFileName(); // Generate zip file name with timestamp

        String zipFilePath = outputFolderPath + "/" + zipFileName;
        loadRecentFiles();
        try {
            FileOutputStream fos = new FileOutputStream(zipFilePath);
            ZipOutputStream zos = new ZipOutputStream(fos);

            // Recursively zip the folder
            zipFolder(sourceFolderPath, zos, "");

            zos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}