package com.androsov.gui.frames.settings;

import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.DefaultFrame;
import com.androsov.gui.frames.EscClosableDefaultFrame;
import com.androsov.node.Node;
import com.androsov.node.NodeManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

class AddExistingNodeFrame extends EscClosableDefaultFrame {
    private static final ViewConfig viewConfig = ViewConfig.getInstance();
    private final NodeManager nodeManager;

    public AddExistingNodeFrame(DefaultFrame parentFrame, Node nodeToRedact) {
        super(viewConfig.getSettingsFrameSizeX(), viewConfig.getSettingsFrameSizeY() / 2, false);

        nodeManager = NodeManager.getInstance();

        JPanel newNodePanel = new JPanel();
        newNodePanel.setLayout(new GridLayout(3, 1));
        this.add(newNodePanel);

        // first allow input pseudonym for new node
        // then show list of nodes to choose from
        // then add new node to current node
        // then save nodes
        // then draw current node
        // then close frame

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

        // list of nodes to choose from
        JPanel nodesListPanel = new JPanel();
        nodesListPanel.setLayout(new GridLayout(1, 2));
        nodesListPanel.setOpaque(false);
        JLabel nodesListLabel = new JLabel();
        nodesListLabel.setText("Выберите страницу");
        nodesListPanel.add(nodesListLabel);
        JComboBox<String> nodesListComboBox = new JComboBox<>();
        HashMap<String, Node> nodesMap = new HashMap<>();
        for (Node node : nodeManager.getNodes()) {
            nodesListComboBox.addItem(node.toString());
            nodesMap.put(node.toString(), node);
        }
        nodesListPanel.add(nodesListComboBox);
        newNodePanel.add(nodesListPanel);

        // button to save new node
        JPanel saveNodePanel = new JPanel();
        saveNodePanel.setLayout(new GridLayout(1, 2));
        saveNodePanel.setOpaque(false);
        JButton saveNodeButton = new JButton("Сохранить");
        saveNodeButton.addActionListener(e1 -> {
            nodeToRedact.addChild(nodesMap.get(nodesListComboBox.getSelectedItem().toString()), pseudonymTextField.getText());
            this.dispose();
            parentFrame.refresh();
            try {
                nodeManager.saveNodes();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Ошибка при сохранении узлов", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });

        saveNodePanel.add(saveNodeButton);
        newNodePanel.add(saveNodePanel);
        this.setVisible(true);
    }
}
