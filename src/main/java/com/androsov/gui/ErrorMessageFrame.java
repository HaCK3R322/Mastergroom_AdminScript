package com.androsov.gui;

import com.androsov.node.Node;
import com.androsov.node.NodeManager;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class ErrorMessageFrame {
    // frame for error message
    JFrame frame;
    JPanel panel;
    String errorMessage;

    public ErrorMessageFrame(String errorMessage) {
        this.errorMessage = errorMessage;

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        frame.setTitle("Ошибка");

        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        frame.add(panel);

        JLabel label = new JLabel("<html><font>" + errorMessage + "</font></html>");
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        panel.add(label);

        frame.setVisible(true);
    }
}
