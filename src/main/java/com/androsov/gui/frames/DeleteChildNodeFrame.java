package com.androsov.gui.frames;

import com.androsov.gui.ViewConfig;
import com.androsov.node.Node;
import com.androsov.node.NodeManager;
import com.androsov.node.NodePseudonym;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

class DeleteChildNodeFrame extends MyFrame {
    private static final ViewConfig viewConfig = ViewConfig.getInstance();
    private final NodeManager nodeManager;

    public DeleteChildNodeFrame(MyFrame parentFrame, Node nodeToRedact) {
        super(viewConfig.getSettingsFrameSizeX(), viewConfig.getSettingsFrameSizeY() / 4, false);

        nodeManager = NodeManager.getInstance();

        JPanel deleteChildNodePanel = new JPanel();
        deleteChildNodePanel.setLayout(new GridLayout(2, 1));
        this.add(deleteChildNodePanel);

        // first allow input pseudonym for new node
        // then show list of nodes to choose from
        // then add new node to current node
        // then save nodes
        // then draw current node
        // then close frame

        // list of nodes to choose from
        JPanel nodesListPanel = new JPanel();
        nodesListPanel.setLayout(new GridLayout(1, 2));
        nodesListPanel.setOpaque(false);
        JLabel nodesListLabel = new JLabel();
        nodesListLabel.setText("Выберите узел");
        nodesListPanel.add(nodesListLabel);
        JComboBox<String> nodesListComboBox = new JComboBox<>();
        HashMap<String, NodePseudonym> nodesPseudonymsMap = new HashMap<>();
        for (NodePseudonym nodePseudonym : nodeToRedact.getChildren()) {
            nodesListComboBox.addItem(nodePseudonym.getPhrase());
            nodesPseudonymsMap.put(nodePseudonym.getPhrase(), nodePseudonym);
        }
        nodesListPanel.add(nodesListComboBox);
        deleteChildNodePanel.add(nodesListPanel);

        // button to save new node
        JPanel saveNodePanel = new JPanel();
        saveNodePanel.setLayout(new GridLayout(1, 2));
        saveNodePanel.setOpaque(false);
        JButton saveNodeButton = new JButton("Удалить");
        saveNodeButton.addActionListener(e1 -> {
            nodeToRedact.removeChild(nodesPseudonymsMap.get(nodesListComboBox.getSelectedItem().toString()));
            this.dispose();
            parentFrame.refresh();
            try {
                nodeManager.saveNodes();
            } catch (Exception e) {
                Logger.getLogger("AdminScriptLogger").log(Level.WARNING, "Ошибка сохранения узлов", e);
                new ErrorMessageFrame("Ошибка при сохранении узлов");
            }
        });

        saveNodePanel.add(saveNodeButton);
        deleteChildNodePanel.add(saveNodePanel);
        this.setVisible(true);
    }
}
