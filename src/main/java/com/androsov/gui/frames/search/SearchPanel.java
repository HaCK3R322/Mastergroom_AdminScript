package com.androsov.gui.frames.search;

import com.androsov.gui.frames.DefaultFrame;
import com.androsov.node.Node;
import com.androsov.node.NodeManager;
import com.androsov.node.NodePseudonym;
import info.clearthought.layout.TableLayout;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class SearchPanel extends JPanel {
    public SearchPanel(DefaultFrame parent) {
        this.setBackground(parent.getBackground());

        final double[][] size = {{0.7, TableLayout.FILL},
                {TableLayout.FILL}};
        this.setLayout(new TableLayout(size));

        NodeManager nodeManager = NodeManager.getInstance();

        JComboBox<String> nodesListComboBox = new JComboBox<>();
        HashMap<String, Node> nodesMap = new HashMap<>();
        for (Node node : nodeManager.getNodes()) {
            for (NodePseudonym nodePseudonym : node.getChildren()) {
                String text = nodePseudonym.getPhrase() + " (id: " + nodePseudonym.getNode().getId() + ")";
                nodesListComboBox.addItem(text);
                nodesMap.put(text, nodePseudonym.getNode());
            }
        }
        this.add(nodesListComboBox, "0, 0");

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
                parent.refresh();
            }
        });
        goToNodePanel.add(goToNodeButton);

        this.add(goToNodePanel, "1, 0");

        this.setMinimumSize(new Dimension(50, 0));
    }
}
