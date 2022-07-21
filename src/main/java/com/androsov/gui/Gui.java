package com.androsov.gui;

import com.androsov.node.Node;
import com.androsov.node.NodeManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Gui {
    JFrame frame;
    JPanel panel;

    public Gui() {

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
            new ScriptFrame();
            frame.setVisible(false);
        });
        startButton.setFont(new Font("Arial", Font.PLAIN, 50));
        panel.add(startButton);

        frame.setVisible(true);
    }
}
