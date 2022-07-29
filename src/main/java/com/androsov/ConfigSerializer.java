package com.androsov;

import com.androsov.util.PathConverter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigSerializer {
    public static Map<String, String> readConfig(String configFilePath) throws IOException {
        String filePath = PathConverter.convertToAbsoluteAppdataFilePath(configFilePath);

        Logger logger = Logger.getLogger("AdminScriptLogger");
        logger.log(Level.INFO, "Reading config file from " + filePath);

        Map<String, String> config = new HashMap<>();
        // create File from path, if it doesn't exist, throw exception
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("Config file not found");
        }

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("#")) {
                continue;
            }
            if (line.contains("=")) {
                logger.log(Level.INFO, "Config file read: " + line);
                String[] parts = line.split("=");
                config.put(parts[0], parts[1]);
            }
        }
        scanner.close();

        logger.log(Level.INFO, "Config file read");

        return config;
    }

    public static void saveConfig(String configFilePath, Map<String, String> config) throws IOException {
        String filePath = PathConverter.convertToAbsoluteAppdataFilePath(configFilePath);

        Logger logger = Logger.getLogger("AdminScriptLogger");
        logger.log(Level.INFO, "Saving config file to " + filePath);

        // create File from path, if it doesn't exist, throw exception
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("Config file not found");
        }
        FileWriter writer = new FileWriter(file);
        for (Map.Entry<String, String> entry : config.entrySet()) {
            logger.log(Level.INFO, "Config file write: " + entry.getKey() + "=" + entry.getValue());
            writer.write(entry.getKey() + "=" + entry.getValue() + "\n");
        }
        writer.close();

        logger.log(Level.INFO, "Config file saved");
    }
}
