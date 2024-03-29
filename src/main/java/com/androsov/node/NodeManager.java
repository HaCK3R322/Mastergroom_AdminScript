package com.androsov.node;

import com.androsov.ConfigSerializer;
import com.androsov.gui.frames.ErrorMessageFrame;
import com.androsov.node.exceptions.NoNodesFoundException;
import com.androsov.node.exceptions.NodeDeserializingException;
import com.androsov.node.exceptions.NodeManagerPropertiesException;
import com.androsov.node.io.NodeSerializer;
import com.androsov.util.PathConverter;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class NodeManagerConfig {
    public static Integer firstNodeId = 1;
    public static String connectionsPath = "resources/connections.json";
    public static String nodesPath = "resources/nodes.json";
    public static boolean sortNodes = true;

    public static void readConfig(String configPath) throws NodeManagerPropertiesException, IOException {
        Map<String, String> propertiesMap = ConfigSerializer.readConfig(configPath);

        if(!propertiesMap.containsKey("firstNodeId") ||
                !propertiesMap.containsKey("connectionsPath") ||
                !propertiesMap.containsKey("nodesPath") ||
                !propertiesMap.containsKey("sortNodes")) {
            throw new NodeManagerPropertiesException("Config file is missing properties.");
        }

        firstNodeId = Integer.parseInt(propertiesMap.get("firstNodeId"));
        connectionsPath = PathConverter.convertToAbsoluteAppdataFilePath(propertiesMap.get("connectionsPath"));
        nodesPath = PathConverter.convertToAbsoluteAppdataFilePath(propertiesMap.get("nodesPath"));
        sortNodes = Boolean.parseBoolean(propertiesMap.get("sortNodes"));
    }

    public static void saveConfig(String configPath) {
        try {
            ConfigSerializer.saveConfig(configPath, getPropertiesStringMap());
        } catch (Exception e) {
            new ErrorMessageFrame("Error saving config file: " + e.getMessage());
        }
    }

    public static Map<String, String> getPropertiesStringMap() {
        Map<String, String> propertiesMap = new HashMap<>();
        propertiesMap.put("firstNodeId", String.valueOf(firstNodeId));
        propertiesMap.put("connectionsPath", connectionsPath);
        propertiesMap.put("nodesPath", nodesPath);
        propertiesMap.put("sortNodes", String.valueOf(sortNodes));
        return propertiesMap;
    }
}

public class NodeManager {
    private Integer sequence = 0;
    private final String configPath = "resources/adminscript.config";
    private static final Logger logger = Logger.getLogger("AdminScriptLogger");

    private static NodeManager instance;
    public static NodeManager getInstance() {
        if (instance == null) {
            instance = new NodeManager();
            logger.info("NodeManager instance created");
        }
        return instance;
    }
    private NodeManager() {
        try {
            readProperties(configPath);
            logger.log(Level.INFO, "Starting node deserialization");
            readNodes(NodeManagerConfig.nodesPath, NodeManagerConfig.connectionsPath);

            // for each node sort children by Pseudonym.phrase
            if (NodeManagerConfig.sortNodes) {
                for (Node node : nodes) {
                    node.getChildren().sort(Comparator.comparing(o -> o.getPhrase().toLowerCase()));
                }
            }
        } catch (NodeManagerPropertiesException e) {
            String errorMessage = "Error: " + e.getMessage();
            logger.log(Level.WARNING, Arrays.toString(e.getStackTrace()));
            new ErrorMessageFrame(errorMessage);
        } catch (NoNodesFoundException e) {
            logger.log(Level.WARNING, "No nodes found in file " + NodeManagerConfig.nodesPath + "; Is it first run?");
            nodes = new ArrayList<>();
            addNode(new Node( "This node generated automatically. You can add your own nodes here."));
        } catch (NodeDeserializingException | IOException | NullPointerException e) {
            String errorMessage = "Could not create NodeManager instance: " + e;
            logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            new ErrorMessageFrame(errorMessage, true);
        }
    }

    private List<Node> nodes;
    private Node currentNode;

    public List<Node> getNodes() {
        return nodes;
    }
    public Node getCurrentNode() {
        return currentNode;
    }
    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    public Node addNode(Node node) {
        node.setId(getNextNodeId());
        nodes.add(node);
        logger.log(Level.INFO, "Added node: " + node);
        return node;
    }
    public void removeNode(Node node) {
        // remove this node from all other nodes' children
        for (Node n : nodes) {
            logger.log(Level.INFO, "Removing  child node " + node.getId() + " from parent node " + n.getId());
            n.removeChildById(node.getId());
        }
        logger.log(Level.INFO, "Removing node " + node.getId());
        nodes.remove(node);
    }

    public Node getNodeById(Integer id) {
        return nodes.stream()
                .filter(node -> node.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Node with id " + id + " not found"));
    }

    public Node getFirstNode() {
        try {
            return getNodeById(NodeManagerConfig.firstNodeId);
        } catch (NoSuchElementException | NullPointerException e) {
            logger.log(Level.SEVERE, "Incorrect firstNodeId in config file. Please, check it.");
            new ErrorMessageFrame("Incorrect first node id in config file [" + configPath + "]. Please, check it.");
            throw new RuntimeException(e);
        }
    }

    public void readNodes(String nodesPath, String connectionsPath) throws IOException, NodeDeserializingException, NoNodesFoundException {
        nodes = NodeSerializer.deserialize(nodesPath, connectionsPath);

        // set sequence
        sequence = nodes.stream()
                .mapToInt(Node::getId)
                .max()
                .orElse(0);
    }

    public void saveNodes() throws IOException {
        NodeSerializer.serialize(nodes, NodeManagerConfig.nodesPath, NodeManagerConfig.connectionsPath);
    }

    public void readProperties(String propertiesPath) throws IOException, NodeManagerPropertiesException {
        NodeManagerConfig.readConfig(configPath);
    }

    // sequence new node id
    public Integer getNextNodeId() {
        return ++sequence;
    }
}
