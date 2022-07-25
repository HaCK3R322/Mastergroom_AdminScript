package com.androsov.gui.frames;

import com.androsov.node.Node;
import com.androsov.node.NodeManager;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class ErrorMessageFrame extends JFrame {
    // this for error message
    JPanel panel;
    String errorMessage;

    public ErrorMessageFrame(String errorMessage) {
        this.errorMessage = errorMessage;

        this.setSize(300, 150);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setTitle("Ошибка");

        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        this.add(panel);

        JLabel label = new JLabel("<html><font>" + errorMessage + "</font></html>");
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        panel.add(label);

        this.setVisible(true);
    }

    public ErrorMessageFrame(String errorMessage, boolean several) {
        if (several)
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.errorMessage = errorMessage;

        this.setSize(300, 150);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setTitle("Ошибка");

        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));
        this.add(panel);

        JLabel label = new JLabel("<html><font>" + errorMessage + "</font></html>");
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        panel.add(label);

        this.setVisible(true);
    }
}
