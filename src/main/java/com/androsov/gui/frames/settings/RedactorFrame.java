package com.androsov.gui.frames.settings;

import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.DefaultFrame;
import com.androsov.node.Node;
import com.androsov.node.NodeManager;

import javax.swing.*;
import java.awt.*;

public class RedactorFrame extends DefaultFrame {
    private static final ViewConfig viewConfig = ViewConfig.getInstance();
    private final NodeManager nodeManager;
    private final DefaultFrame parentFrame;
    
    public RedactorFrame(DefaultFrame parentFrame) {
        super(viewConfig.getSettingsFrameSizeX(), viewConfig.getSettingsFrameSizeY(), false);

        this.parentFrame = parentFrame;

        nodeManager = NodeManager.getInstance();
        Node nodeToRedact = nodeManager.getCurrentNode();

        this.setTitle("Редактирование узла id: " + nodeToRedact.getId());

        JPanel redactorPanel = new JPanel();
        redactorPanel.setOpaque(true);
        this.setBackground(viewConfig.getBackgroundColor());
        redactorPanel.setLayout(new GridLayout(5, 1));
        this.add(redactorPanel);

        // button that opens phrase redactor frame
        JButton phraseRedactorButton = new JButton("Изменить фразу");
        phraseRedactorButton.addActionListener(e -> {
            new PhraseRedactorFrame(this, nodeToRedact);
        });
        redactorPanel.add(phraseRedactorButton);


        // button to open new frame to create new node and its pseudonym
        JPanel newNodePanel = new JPanel();
        newNodePanel.setLayout(new GridLayout(1, 2));
        newNodePanel.setOpaque(false);
        JButton newNodeButton = new JButton("Создать новую страницу");
        newNodeButton.addActionListener(e -> {
            new AddNewNodeFrame(parentFrame, nodeToRedact);
        });
        newNodePanel.add(newNodeButton);
        redactorPanel.add(newNodePanel);

        // button to open new frame to add existing node to current node
        JPanel addNodePanel = new JPanel();
        addNodePanel.setLayout(new GridLayout(1, 2));
        addNodePanel.setOpaque(false);
        JButton addNodeButton = new JButton("Добавить ссылку на существующую страницу");
        addNodeButton.addActionListener(e -> {
            new AddExistingNodeFrame(this, nodeToRedact);
        });
        addNodePanel.add(addNodeButton);
        redactorPanel.add(addNodePanel);

        // button to choose and delete child node from current node
        JPanel deleteChildNodePanel = new JPanel();
        deleteChildNodePanel.setLayout(new GridLayout(1, 2));
        deleteChildNodePanel.setOpaque(false);
        JButton deleteChildNodeButton = new JButton("Удалить ссылку на страницу");
        deleteChildNodeButton.addActionListener(e -> {
            new DeleteChildNodeFrame(this, nodeToRedact);
        });
        deleteChildNodePanel.add(deleteChildNodeButton);
        redactorPanel.add(deleteChildNodePanel);

        // button to delete current from all nodes children
        // then delete current node
        JPanel deleteNodePanel = new JPanel();
        deleteNodePanel.setLayout(new GridLayout(1, 2));
        deleteNodePanel.setOpaque(false);
        JButton deleteNodeButton = new JButton("Удалить эту страницу");
        // show dialog to confirm deletion of node
        deleteNodeButton.addActionListener(e -> {
            int dialogResult = JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить эту страницу?", "Удаление страницы", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                nodeManager.removeNode(nodeManager.getCurrentNode());
                nodeManager.setCurrentNode(nodeManager.getFirstNode());
                parentFrame.refresh();
            }
        });
        deleteNodePanel.add(deleteNodeButton);
        redactorPanel.add(deleteNodePanel);

        this.setVisible(true);
    }

    @Override
    public void refresh() {
        parentFrame.refresh();
    }
}
