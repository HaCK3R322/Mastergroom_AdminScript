package com.androsov.gui.frames;

import com.androsov.node.Node;
import com.androsov.node.NodeManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

public class ErrorMessageFrame extends JFrame {
    // this for error message
    private final JPanel panel = new JPanel();
    String errorMessage;

    public ErrorMessageFrame(String errorMessage) {
        this.errorMessage = errorMessage;

        this.setSize(300, 150);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setTitle("Ошибка");

        panel.setLayout(new GridLayout(1, 1));
        this.add(panel);

        JLabel label = new JLabel("<html><font>" + errorMessage + "</font></html>");
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        panel.add(label);

        this.setVisible(true);
    }

    public ErrorMessageFrame(String errorMessage, boolean several) {
        if (several){
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.setBackground(Color.RED);
            this.panel.setBackground(Color.RED);
            // add listener to close window when click on close button
            this.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0xD);
                }
            });
        }
        this.errorMessage = errorMessage;

        this.setSize(300, 150);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
        this.setTitle("Ошибка");

        panel.setLayout(new GridLayout(1, 1));
        this.add(panel);

        JLabel label = new JLabel("<html><font>" + errorMessage + "</font></html>");
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        panel.add(label);

        this.setVisible(true);
    }
}
