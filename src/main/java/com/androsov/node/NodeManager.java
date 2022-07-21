package com.androsov.node;

import com.androsov.gui.ErrorMessageFrame;
import com.androsov.node.exceptions.NoNodesFoundException;
import com.androsov.node.exceptions.NodeDeserializingException;
import com.androsov.node.exceptions.NodeManagerPropertiesException;
import com.androsov.node.io.NodeSerializer;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

class NodeManagerConfig {
    public static Integer firstNodeId = 0;

    // read all properties from config file
    public static void readConfig() throws NodeManagerPropertiesException {
        Logger logger = Logger.getLogger("AdminScriptLogger");
        logger.log(Level.INFO, "Reading config file");
        try {
            // create File from path, if it doesn't exist, throw exception
            File file = new File("resources/adminscript.config");
            if (!file.exists()) {
                throw new NodeManagerPropertiesException("Config file not found");
            }

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("firstNodeId=")) {
                    firstNodeId = Integer.parseInt(line.substring("firstNodeId=".length()));
                }
            }
            scanner.close();

            logger.log(Level.INFO, "Config file read");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading config file");
        }
    }
}

public class NodeManager {
    private Integer sequence = 0;
    private final String nodesPath = "resources/nodes.json";
    private String connectionsPath = "resources/connections.json";
    private String propertiesPath = "resources/adminscript.properties";
    private static Logger logger = Logger.getLogger("AdminScriptLogger");


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
            readProperties(propertiesPath);
            readNodes(nodesPath, connectionsPath);
        } catch (NodeManagerPropertiesException | NodeDeserializingException | IOException e) {
            logger.log(Level.SEVERE, "Could not create NodeManager instance: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (NoNodesFoundException e) {
            logger.log(Level.WARNING, "No nodes found in file " + nodesPath + "; Is it first run?");
            nodes = new ArrayList<>();
            addNode(new Node( "This node generated automatically. You can add your own nodes here."));
        }
    }

    private List<Node> nodes;

    public List<Node> getNodes() {
        return nodes;
    }

    public Node addNode(Node node) {
        node.setId(getNextNodeId());
        nodes.add(node);
        return node;
    }
    public void removeNode(Node node) {
        // remove this node from all other nodes' children
        for (Node n : nodes) {
            n.removeChildById(node.getId());
        }
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
        } catch (NoSuchElementException e) {
            logger.log(Level.SEVERE, "Incorrect firstNodeId in config file. Please, check it.");
            new ErrorMessageFrame("Incorrect first node id in config file [" + propertiesPath + "]. Please, check it.");
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
        NodeSerializer.serialize(nodes, nodesPath, connectionsPath);
    }

    public void readProperties(String propertiesPath) throws IOException, NodeManagerPropertiesException {
        NodeManagerConfig.readConfig();
    }

    // sequence new node id
    public Integer getNextNodeId() {
        return ++sequence;
    }
}
