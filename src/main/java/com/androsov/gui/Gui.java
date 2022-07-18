package com.androsov.gui;

import com.androsov.node.Node;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Gui {
    JFrame frame;
    JPanel panel;
    List<Node> nodes;

    public Gui(List<Node> nodes) {
        this.nodes = nodes;

        // Create and set up the window.
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        frame.add(panel);

        // start button
        JButton startButton = new JButton("Начать");
        startButton.addActionListener(e -> {
            // if nodes list is empty, then show error message
            if (nodes.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No nodes to start from", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                // if nodes list is not empty, then draw first node
                new ScriptFrame(nodes.get(0));
            }
        });
        startButton.setFont(new Font("Arial", Font.PLAIN, 50));
        panel.add(startButton);

        frame.setVisible(true);
    }
}
