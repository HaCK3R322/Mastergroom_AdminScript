package com.androsov.gui;

import com.androsov.node.Node;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class NodeManagerFrame {
    List<Node> nodes;
    JFrame frame;
    JPanel panel;

    public NodeManagerFrame(List<Node> nodes) {
        this.nodes = nodes;

        // Create and set up the window.
        frame = new JFrame();
        panel = new JPanel();
        frame.add(panel);

        // set frame size 800x800px and center it on the screen
        frame.setSize(800, 800);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        final int x = dim.width/2-frame.getSize().width/2 + 100;
        final int y = dim.height/2-frame.getSize().height/2 + 100;
        frame.setLocation(x, y);

        frame.setVisible(true);
    }
}
