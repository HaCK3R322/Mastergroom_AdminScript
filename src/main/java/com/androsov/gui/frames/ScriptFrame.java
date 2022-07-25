package com.androsov.gui.frames;

import com.androsov.gui.ViewConfig;
import com.androsov.node.Node;
import com.androsov.node.NodeManager;
import com.androsov.node.NodePseudonym;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScriptFrame extends MyFrame {
    public final JPanel panel;
    private final JMenuBar menuBar;
    private final Logger logger;
    private final NodeManager nodeManager;
    private Node currentNode;
    // history of direct node moving (to go back)
    private final ArrayList<Node> lastNodesList = new ArrayList<>();
    // history of indirect node moving (to go back)
    private final ArrayList<Node> rollbackLastNodesList = new ArrayList<>();

    private static final ViewConfig viewConfig = ViewConfig.getInstance();

    public ScriptFrame() {
        super(viewConfig.getFrameSizeX(), viewConfig.getFrameSizeY(), false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        logger = Logger.getLogger("AdminScriptLogger");
        nodeManager = NodeManager.getInstance();

        panel = new JPanel();
        this.add(panel);

        // add menu bar to frame
        menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        this.setJMenuBar(menuBar);
        configureMenuBarContent();

        currentNode = nodeManager.getFirstNode();
        lastNodesList.add(currentNode);
        drawCurrentNode();

        addNodeKeyMovementsToFrame();

        this.setVisible(true);
    }

    private void addNodeKeyMovementsToFrame() {
        setFocusable(true);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {}

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    goToPreviousClickedNode();
                    drawCurrentNode();
                } if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    goCloserToLastClickedNode();
                    drawCurrentNode();
                } if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    goToStartNode();
                    drawCurrentNode();
                }
            }
        });
    }

    private void goToPreviousClickedNode() {
        if (lastNodesList.size() > 1) {
            currentNode = lastNodesList.get(lastNodesList.size() - 2);
            rollbackLastNodesList.add(lastNodesList.get(lastNodesList.size() - 1));
            lastNodesList.remove(lastNodesList.size() - 1);
        }
    }
    private void goCloserToLastClickedNode() {
        if (rollbackLastNodesList.size() > 0) {
            lastNodesList.add(rollbackLastNodesList.get(rollbackLastNodesList.size() - 1));
            rollbackLastNodesList.remove(rollbackLastNodesList.size() - 1);
            currentNode = lastNodesList.get(lastNodesList.size() - 1);
        }
    }

    // go to start node
    private void goToStartNode() {
        // show dialog yes/no
        int result = JOptionPane.showConfirmDialog(
                this,
                "Вы уверенны что хотите вернуться в начало?",
                "В начало",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            currentNode = nodeManager.getFirstNode();
            lastNodesList.clear();
            rollbackLastNodesList.clear();
            lastNodesList.add(currentNode);
        }
    }

    @Override
    public void refresh() {
        drawCurrentNode();
    }
    private void drawCurrentNode() {
        panel.removeAll();

        panel.setLayout(new GridLayout(1, 2));

        JPanel phrasePanel = new JPanel();
        phrasePanel.setLayout(new GridLayout(1, 1));
        JLabel phraseLabel = new JLabel();
        phrasePanel.add(phraseLabel);
        phraseLabel.setText(getHtmlText(currentNode.getPhrase(), viewConfig.getTextFontSize()));
        phraseLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(phrasePanel);
        phraseLabel.setOpaque(true);
        phraseLabel.setBackground(viewConfig.getBackgroundColor());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(10, 1));
        panel.add(buttonsPanel);
        buttonsPanel.setOpaque(true);
        buttonsPanel.setBackground(viewConfig.getBackgroundColor());

        // buttons to connect to other nodes
        // every click saves to lastNodes list
        for (NodePseudonym pseudonym : currentNode.getChildren()) {
            JButton button = new JButton();
            button.setText(getHtmlText(pseudonym.getPhrase(), viewConfig.getButtonsFontSize()));
            button.addActionListener(e -> {
                currentNode = pseudonym.getNode();
                lastNodesList.add(currentNode);
                rollbackLastNodesList.clear();
                drawCurrentNode();
            });
            buttonsPanel.add(button);
        }

        panel.revalidate();
        this.repaint();
    }

    private String getHtmlText(String text, Integer fontSize) {
        return "<html><p style='font-size:" + fontSize + "'>" + text + "</p></html>";
    }

    private void configureMenuBarContent() {
        // add menu "Settings"
        JMenu settingsMenu = new JMenu("Настройки");
        settingsMenu.setBackground(Color.WHITE);
        menuBar.add(settingsMenu);

        // View
        JMenuItem settingsMenuItem = new JMenuItem("Вид");
        settingsMenuItem.addActionListener(e -> {
            new ViewSettingsFrame(this);
        });
        settingsMenu.add(settingsMenuItem);

        // Redactor
        JMenuItem redactorMenuItem = new JMenuItem("Редакитровать этот узел");
        redactorMenuItem.addActionListener(e -> {
            new RedactorFrame(this);
        });
        settingsMenu.add(redactorMenuItem);
    }

}