package com.androsov.node.io;

import com.androsov.node.Node;
import com.androsov.node.NodePseudonym;
import com.androsov.node.exceptions.NoNodesFoundException;
import com.androsov.node.exceptions.NodeDeserializingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NodeSerializer {
    private static final Logger logger = Logger.getLogger("AdminScriptLogger");


    @AllArgsConstructor
    private static class NodeConnection {
        @Expose
        @Getter @Setter public int parentId;
        @Expose
        @Getter @Setter public int childId;
        @Expose
        @Getter @Setter public String phrase;
    }

    public static void serialize(List<Node> nodes, String filepathNodes, String filepathPseudonyms) throws IOException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        writeToFile(filepathNodes, gson.toJson(nodes));

        List<NodeConnection> nodeConnections = new ArrayList<>();
        for (Node node : nodes) {
            for (NodePseudonym nodePseudonym : node.getChildren()) {
                nodeConnections.add(new NodeConnection(node.getId(), nodePseudonym.getNode().getId(), nodePseudonym.getPhrase()));
            }
        }
        writeToFile(filepathPseudonyms, gson.toJson(nodeConnections));

        logger.log(Level.INFO, "Nodes serialized to " + filepathNodes + " and " + filepathPseudonyms);
    }

    public static List<Node> deserialize(String filePathNodes, String filePathPseudonyms) throws IOException, NodeDeserializingException, NoNodesFoundException {
        GsonBuilder gson = new GsonBuilder();
        Type listType = new com.google.gson.reflect.TypeToken<List<Node>>() {}.getType();
        Type listTypePseudonyms = new com.google.gson.reflect.TypeToken<List<NodeConnection>>() {}.getType();
        Gson gsonBuilder = gson.create();


        // parse nodes
        String jsonInput = readFromFile(filePathNodes);
        List<Node> nodes = gsonBuilder.fromJson(jsonInput, listType);
        // init children lists
        if(nodes.size() < 1) {
            throw new NoNodesFoundException("No nodes found in file " + filePathNodes);
        }
        for (Node node :
                nodes) {
            node.setChildren(new ArrayList<>());
        }
        // log all nodes
        for (Node node :
             nodes) {
            logger.info("Got node: " + node.toString());
        }

        // parse pseudonyms
        String jsonInputPseudonyms = readFromFile(filePathPseudonyms);
        List<NodeConnection> nodeConnections = gsonBuilder.fromJson(jsonInputPseudonyms, listTypePseudonyms);

        // connect nodes
        for (NodeConnection nodeConnection : nodeConnections) {
            try {
                Node parent = nodes.stream()
                        .filter(node -> node.getId().equals(nodeConnection.getParentId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("PARENTING EXCEPTION: No node with id " + nodeConnection.getParentId()));
                Node child = nodes.stream()
                        .filter(node -> node.getId().equals(nodeConnection.getChildId()))
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("CHILDING EXCEPTION: No node with id " + nodeConnection.getChildId()));
                logger.info("Connecting node: parentId = " + parent.getId() + ", childId = " + child.getId() + ", phrase = " + nodeConnection.getPhrase());
                parent.addChild(child, nodeConnection.getPhrase());
            } catch (NoSuchElementException e) {
                logger.warning(e.getMessage());
                throw new NodeDeserializingException(e.getMessage());
            }
        }

        logger.info("Nodes deserialized successfully");
        return nodes;
    }
    
    private static String readFromFile(String filePath) throws IOException {
        File file = new File(filePath);

        if (!file.exists()) {
            throw new FileNotFoundException("File " + filePath + " not found.");
        }

        Scanner scanner = new Scanner(file);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
        }
        scanner.close();
        return stringBuilder.toString();
    }

    private static void writeToFile(String filePath, String content) throws IOException {
        File file = new File(filePath);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.close();
    }
}
