package com.androsov.gui.frames.settings;

import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.ErrorMessageFrame;
import com.androsov.gui.frames.DefaultFrame;
import com.androsov.gui.frames.EscClosableDefaultFrame;
import com.androsov.node.Node;
import com.androsov.node.NodeManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class AddNewNodeFrame extends EscClosableDefaultFrame {
    private static final ViewConfig viewConfig = ViewConfig.getInstance();
    private final NodeManager nodeManager;

    public AddNewNodeFrame(DefaultFrame parentFrame, Node nodeToRedact) {
        super(viewConfig.getSettingsFrameSizeX(), viewConfig.getSettingsFrameSizeY() / 2, false);

        nodeManager = NodeManager.getInstance();

        JPanel newNodePanel = new JPanel();
        newNodePanel.setLayout(new GridLayout(3, 1));
        this.add(newNodePanel);

        // text field for node's pseudonym
        JPanel pseudonymPanel = new JPanel();
        pseudonymPanel.setLayout(new GridLayout(1, 2));
        pseudonymPanel.setOpaque(false);
        JLabel pseudonymLabel = new JLabel();
        pseudonymLabel.setText("Название ссылки");
        pseudonymPanel.add(pseudonymLabel);
        JTextField pseudonymTextField = new JTextField();
        pseudonymTextField.setText("");
        pseudonymPanel.add(pseudonymTextField);
        newNodePanel.add(pseudonymPanel);

        // text field for node's phrase
        JPanel phrasePanel = new JPanel();
        phrasePanel.setLayout(new GridLayout(1, 2));
        phrasePanel.setOpaque(false);
        JLabel phraseLabel = new JLabel();
        phraseLabel.setText("Фраза");
        phrasePanel.add(phraseLabel);
        JTextField phraseTextField = new JTextField();
        phraseTextField.setText("");
        phrasePanel.add(phraseTextField);
        newNodePanel.add(phrasePanel);

        // button to save new node
        JPanel saveNodePanel = new JPanel();
        saveNodePanel.setLayout(new GridLayout(1, 2));
        saveNodePanel.setOpaque(false);
        JButton saveNodeButton = new JButton("Сохранить");
        saveNodeButton.addActionListener(e1 -> {
            Node newNode = nodeManager.addNode(new Node(phraseTextField.getText()));
            nodeToRedact.addChild(newNode, pseudonymTextField.getText());
            this.dispose();
            parentFrame.refresh();
            try {
                nodeManager.saveNodes();
            } catch (IOException ex) {
                Logger.getLogger("AdminScriptLogger").log(Level.WARNING, "Could not save nodes: " + ex.getMessage());
                new ErrorMessageFrame("Could not save nodes: " + ex.getMessage());
            }
        });
        saveNodePanel.add(saveNodeButton);
        newNodePanel.add(saveNodePanel);
        this.setVisible(true);
    }
}
