package com.androsov.gui.frames.help;

import com.androsov.util.PathConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelpManager {
    @ToString
    @AllArgsConstructor
    public static class HelpNode {
        @Getter
        @Setter
        public Integer nodeId;
        @Getter
        @Setter
        public String helpTopic;
        @Getter
        @Setter
        public String helpText;
    }

    private static final String helpPath = PathConverter.convertToAbsoluteAppdataFilePath("resources/help.json");
    private static Map<Integer, HelpNode> helpMap = null;

    public static void read() throws IOException {
        helpMap = new HashMap<>();
        Logger.getLogger("AdminScriptLogger").log(Level.INFO, "Reading help from " + helpPath);

        GsonBuilder gson = new GsonBuilder();
        Type listType = new com.google.gson.reflect.TypeToken<List<HelpNode>>() {}.getType();
        Gson gsonBuilder = gson.create();

        try {
            List<HelpNode> helpNodes = gsonBuilder.fromJson(new FileReader(helpPath), listType);
            Integer sequence = 0; // only for collecting all help nodes instead of single node, maybe ill change it later but probably not
            for (HelpNode helpNode : helpNodes) {
                if(helpNode == null) continue;

                if(helpNode.nodeId == null) {
                    sequence--;
                    helpMap.put(sequence, helpNode);
                    continue;
                }

                helpMap.put(helpNode.getNodeId(), helpNode);
            }
        } catch (JsonSyntaxException e) {
            Logger.getLogger("AdminScriptLogger").log(Level.SEVERE, "Error reading help file: " + e.getMessage());
            throw new IOException("Error reading help file: " + e.getMessage());
        }
    }

    public static void save() throws IOException {
        GsonBuilder gson = new GsonBuilder();
        Gson gsonBuilder = gson.setPrettyPrinting().create();

        String json = gsonBuilder.toJson(helpMap.values());
        Logger.getLogger("AdminScriptLogger").log(Level.INFO, "Saving help map");

        File file = new File(helpPath);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(json);
        fileWriter.close();
    }

    public static boolean isHelpExists(Integer nodeId) {
        return getHelpMap().containsKey(nodeId);
    }

    public static Map<Integer, HelpNode> getHelpMap() {
        if(helpMap == null) {
            try {
                Logger.getLogger("AdminScriptLogger")
                        .log(Level.INFO, "Reading help from " + helpPath);
                read();
            } catch (IOException e) {
                Logger.getLogger("AdminScriptLogger")
                        .log(Level.WARNING, "Error reading help from " + helpPath  + ": " + e.getMessage());
                helpMap = new HashMap<>();
            }
        }
        return helpMap;
    }
}
