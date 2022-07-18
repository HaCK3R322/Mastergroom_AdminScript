package com.androsov;

import com.androsov.gui.Gui;
import com.androsov.gui.ScriptFrame;
import com.androsov.node.Node;
import com.androsov.node.NodePseudonym;
import com.androsov.node.io.NodeSerializer;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            final String nodesPath = "resources/nodes.json";
            final String connectionsPath = "resources/connections.json";
            List<Node> nodes = NodeSerializer.deserialize(nodesPath, connectionsPath);
            new Gui(nodes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
