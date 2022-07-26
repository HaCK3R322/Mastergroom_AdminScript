package com.androsov.gui.frames.help;

import com.androsov.ConfigSerializer;
import com.androsov.node.Node;
import com.androsov.node.io.NodeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
    public class HelpNode {
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

    private static final String helpPath = "resources/help.json";
    private static Map<Integer, HelpNode> helpMap = null;
    private static Integer sequence = 0;

    public static void read() throws IOException {
        helpMap = new HashMap<>();

        GsonBuilder gson = new GsonBuilder();
        Type listType = new com.google.gson.reflect.TypeToken<List<HelpNode>>() {}.getType();
        Gson gsonBuilder = gson.create();

        List<HelpNode> helpNodes = gsonBuilder.fromJson(new FileReader(helpPath), listType);

        for (HelpNode helpNode : helpNodes) {
            Logger.getLogger("AdminScriptLogger").log(Level.INFO, "Help node: " + helpNode.toString());
            if (helpNode.getNodeId() == null) {
                helpNode.setNodeId(getSequence());
            }
            helpMap.put(helpNode.getNodeId(), helpNode);
        }
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

    private static Integer getSequence() {
        return --sequence;
    }
}
