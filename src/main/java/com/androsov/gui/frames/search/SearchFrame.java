package com.androsov.gui.frames.search;

import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.DefaultFrame;
import com.androsov.gui.frames.EscClosableDefaultFrame;
import com.androsov.node.Node;
import com.androsov.node.NodeManager;
import com.androsov.node.NodePseudonym;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import javax.swing.*;
import java.awt.GridLayout;
import java.util.HashMap;

public class SearchFrame extends EscClosableDefaultFrame {
    private static final ViewConfig viewConfig = ViewConfig.getInstance();

    public SearchFrame(DefaultFrame parentFrame) {
        super(viewConfig.getSettingsFrameSizeX(), viewConfig.getSettingsFrameSizeY() / 2, false);

        this.setTitle("Поиск");

        NodeManager nodeManager = NodeManager.getInstance();

        JPanel searchNodePanel = new JPanel();
        searchNodePanel.setLayout(new GridLayout(2, 1));
        searchNodePanel.setOpaque(true);
        searchNodePanel.setBackground(viewConfig.getBackgroundColor());
        this.add(searchNodePanel);

        JPanel nodesListPanel = new JPanel();
        nodesListPanel.setLayout(new GridLayout(1, 2));
        nodesListPanel.setOpaque(false);
        JLabel nodesListLabel = new JLabel();
        nodesListLabel.setText("Выберите страницу");
        nodesListPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        nodesListPanel.add(nodesListLabel);

        JComboBox<String> nodesListComboBox = new JComboBox<>();
        HashMap<String, Node> nodesMap = new HashMap<>();
        for (Node node : nodeManager.getNodes()) {
            for (NodePseudonym nodePseudonym : node.getChildren()) {
                String text = nodePseudonym.getPhrase() + " (id: " + nodePseudonym.getNode().getId() + ")";
                nodesListComboBox.addItem(text);
                nodesMap.put(text, nodePseudonym.getNode());
            }
        }
        nodesListPanel.add(nodesListComboBox);
        searchNodePanel.add(nodesListPanel);

        nodesListComboBox.setEditable(true);
        AutoCompleteDecorator.decorate(nodesListComboBox, new ObjectToStringConverter() {
            @Override
            public String getPreferredStringForItem(Object o) {
                return o.toString();
            }
        });

        JPanel goToNodePanel = new JPanel();
        goToNodePanel.setLayout(new GridLayout(1, 2));
        goToNodePanel.setOpaque(false);
        JButton goToNodeButton = new JButton("Перейти");
        goToNodeButton.addActionListener(e1 -> {
            if (nodesListComboBox.getSelectedItem() != null && nodesMap.containsKey(nodesListComboBox.getSelectedItem().toString())) {
                nodeManager.setCurrentNode(nodesMap.get(nodesListComboBox.getSelectedItem().toString()));
                this.dispose();
                parentFrame.refresh();
            }
        });

        goToNodePanel.add(goToNodeButton);
        searchNodePanel.add(goToNodePanel);

        this.setVisible(true);
    }
}
