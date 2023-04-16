//package de.bennocrafter.dataworker.io;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Properties;
//
//public class ReadProperties {
//    // todo fix all of it in this file
//    private List<String> recentFiles = new ArrayList<>();
//    int maxNumberOfBackups;
//    public static final String DATAWORKER_PROPERTIES = "dataworker.properties";
//
//    public static void main(String[] args) throws Exception {
//        ReadProperties propApp = new ReadProperties();
//        propApp.readProperties();
//        System.out.println(propApp.maxNumberOfBackups);
//        System.out.println(propApp.recentFiles);
//    }
//
//    void readProperties() throws Exception{
//        Properties properties = new Properties();
//        Path propertyFile = Paths.get(DATAWORKER_PROPERTIES);
//        try (BufferedReader reader = Files.newBufferedReader(propertyFile, StandardCharsets.UTF_8)) {
//            properties.load(reader);
//            String recentFilesString = properties.getProperty("recentfiles");
//            maxNumberOfBackups = Integer.parseInt(properties.getProperty("maxNumberOfBackups"));
//            if (recentFilesString != null) {
//                recentFiles = Arrays.asList(recentFilesString.split(","));
//            }
//        }
//    }
//}
//
