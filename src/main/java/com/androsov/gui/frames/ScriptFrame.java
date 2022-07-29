package com.androsov.gui.frames;

import com.androsov.Main;
import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.help.HelpFrame;
import com.androsov.gui.frames.help.HelpManager;
import com.androsov.gui.frames.help.HelpMenu;
import com.androsov.gui.frames.help.HelpRedactorFrame;
import com.androsov.gui.frames.search.SearchFrame;
import com.androsov.gui.frames.settings.RedactorFrame;
import com.androsov.gui.frames.settings.ViewSettingsFrame;
import com.androsov.node.Node;
import com.androsov.node.NodeManager;
import com.androsov.node.NodePseudonym;
import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScriptFrame extends DefaultFrame {
    final static double goldenRatio = 0.618033;

    public final JPanel mainPanel;
    private final JMenuBar menuBar;
    private final NodeManager nodeManager;
    // history of direct node moving (to go back)
    private final ArrayList<Node> lastNodesList = new ArrayList<>();
    // history of indirect node moving (to go back)
    private final ArrayList<Node> rollbackLastNodesList = new ArrayList<>();

    private static final ViewConfig viewConfig = ViewConfig.getInstance();

    public ScriptFrame() {
        super(viewConfig.getFrameSizeX(), viewConfig.getFrameSizeY(), true, viewConfig.getFramePosX(), viewConfig.getFramePosY());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        nodeManager = NodeManager.getInstance();

        mainPanel = new JPanel();
        this.add(mainPanel);

        // add menu bar to frame
        menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        this.setJMenuBar(menuBar);
        configureMenuBarContent();

        nodeManager.setCurrentNode(nodeManager.getFirstNode());
        lastNodesList.add(nodeManager.getCurrentNode());
        drawCurrentNode();

        addNodeKeyMovementsToFrame();

        this.setVisible(true);

        // on exit save settings
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (viewConfig.getSavePos()) {
                    viewConfig.setFrameSizeX(getWidth());
                    viewConfig.setFrameSizeY(getHeight());
                    viewConfig.setFramePosX(getX());
                    viewConfig.setFramePosY(getY());
                    viewConfig.saveConfig();
                }
            }
        });
    }

    private void addNodeKeyMovementsToFrame() {
        setFocusable(true);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F && e.isControlDown()) {
                    new SearchFrame(ScriptFrame.this);
                }
                if (e.getKeyCode() == KeyEvent.VK_H && !e.isControlDown()) {
                    if (HelpManager.getHelpMap().containsKey(nodeManager.getCurrentNode().getId())) {
                        HelpManager.HelpNode helpNode = HelpManager.getHelpMap().get(nodeManager.getCurrentNode().getId());
                        new HelpFrame(helpNode.getHelpTopic(), helpNode.getHelpText());
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_H && e.isControlDown()) {
                    new HelpRedactorFrame(ScriptFrame.this, nodeManager.getCurrentNode().getId());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    goToPreviousClickedNode();
                    drawCurrentNode();
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    goCloserToLastClickedNode();
                    drawCurrentNode();
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    goToStartNode();
                    drawCurrentNode();
                }
            }
        });
    }

    private void configureMenuBarContent() {
        // add menu "Settings"
        JMenu settingsMenu = new JMenu("Настройки");
        settingsMenu.setBackground(Color.WHITE);
        menuBar.add(settingsMenu);

        menuBar.add(new HelpMenu("Помощь"));

        JMenu searchMenu = new JMenu("Поиск");
        JMenuItem searchItem = new JMenuItem("Поиск по страницам");
        searchItem.addActionListener(e -> new SearchFrame(ScriptFrame.this));
        searchMenu.add(searchItem);
        JMenuItem goToStartItem = new JMenuItem("Перейти к началу");
        goToStartItem.addActionListener(e -> {
            goToStartNode();
            drawCurrentNode();
        });
        searchMenu.add(goToStartItem);
        menuBar.add(searchMenu);

        // View
        JMenuItem settingsMenuItem = new JMenuItem("Вид");
        settingsMenuItem.addActionListener(e -> new ViewSettingsFrame(this));
        settingsMenu.add(settingsMenuItem);

        // Redactor
        JMenuItem redactorMenuItem = new JMenuItem("Редакитровать эту страницу");
        redactorMenuItem.addActionListener(e -> new RedactorFrame(this));
        settingsMenu.add(redactorMenuItem);
    }

    private void goToPreviousClickedNode() {
        if (lastNodesList.size() > 1) {
            nodeManager.setCurrentNode(lastNodesList.get(lastNodesList.size() - 2));
            rollbackLastNodesList.add(lastNodesList.get(lastNodesList.size() - 1));
            lastNodesList.remove(lastNodesList.size() - 1);
        }
    }

    private void goCloserToLastClickedNode() {
        if (rollbackLastNodesList.size() > 0) {
            lastNodesList.add(rollbackLastNodesList.get(rollbackLastNodesList.size() - 1));
            rollbackLastNodesList.remove(rollbackLastNodesList.size() - 1);
            nodeManager.setCurrentNode(lastNodesList.get(lastNodesList.size() - 1));
        }
    }

    private void goToStartNode() {
        // show dialog yes/no
        int result = JOptionPane.showConfirmDialog(
                this,
                "Вы уверенны что хотите вернуться в начало?",
                "В начало",
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            nodeManager.setCurrentNode(nodeManager.getFirstNode());
            lastNodesList.clear();
            rollbackLastNodesList.clear();
            lastNodesList.add(nodeManager.getCurrentNode());
        }
    }
    @Override
    public void refresh() {
        drawCurrentNode();
    }

    private void drawCurrentNode() {
        mainPanel.removeAll();

        this.setBackground(viewConfig.getBackgroundColor());

        JLabel phraseLabel = new JLabel();
        phraseLabel.setText(getHtmlText(nodeManager.getCurrentNode().getPhrase(), viewConfig.getTextFontSize()));
        phraseLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel phrasePanel = new JPanel();
        phrasePanel.setBackground(viewConfig.getBackgroundColor());

        if(HelpManager.isHelpExists(nodeManager.getCurrentNode().getId()) && viewConfig.getShowHelp()) {
            double[][] phrasePanelTableSizes = {{goldenRatio, TableLayout.FILL}, {TableLayout.FILL}};
            phrasePanel.setLayout(new TableLayout(phrasePanelTableSizes));

            phrasePanel.add(phraseLabel,"0, 0");

            HelpManager.HelpNode helpNode = HelpManager.getHelpMap().get(nodeManager.getCurrentNode().getId());
            String text = helpNode.getHelpTopic() + ":\n" + helpNode.getHelpText();
            JTextArea helpTextPane = new JTextArea();
            helpTextPane.setWrapStyleWord(true);
            helpTextPane.setLineWrap(true);
            helpTextPane.setText(text);
            helpTextPane.setFont(new Font("Default", Font.ITALIC, (int)(viewConfig.getTextFontSize() * goldenRatio)));
            helpTextPane.setEditable(false);
            helpTextPane.setBackground(viewConfig.getBackgroundColor());
            helpTextPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JScrollPane helpTextScrollPane = new JScrollPane(helpTextPane);
            helpTextScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            helpTextScrollPane.setBackground(viewConfig.getBackgroundColor());
            helpTextScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            helpTextScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

            phrasePanel.add(helpTextScrollPane, "1, 0");
        } else {
            phrasePanel.setLayout(new GridLayout(1, 1));
            phrasePanel.add(phraseLabel);
        }

        double[][] mainPanelSize = {{TableLayout.FILL},{TableLayout.FILL, goldenRatio}};
        mainPanel.setLayout(new TableLayout(mainPanelSize));

        mainPanel.add(phrasePanel, "0,0");

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setOpaque(true);
        buttonsPanel.setBackground(viewConfig.getBackgroundColor());

        if(nodeManager.getCurrentNode().getChildren().size() > 10) {
            int numberOfChildren = nodeManager.getCurrentNode().getChildren().size();

            JPanel buttonsContainerPanel = new JPanel();
            buttonsContainerPanel.setLayout(new BorderLayout());

            // like a 5x2 grid, but as many times higher as the number of times there are more than 10 children
            buttonsContainerPanel.setPreferredSize(new Dimension(this.getWidth(),
                    (int)(this.getHeight() * goldenRatio * numberOfChildren / 5)));

            buttonsPanel.setLayout(new GridLayout(numberOfChildren, 1));
            buttonsContainerPanel.add(buttonsPanel, BorderLayout.CENTER);

            JScrollPane buttonsScrollPane = new JScrollPane(buttonsContainerPanel);
            buttonsScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            buttonsScrollPane.setBackground(viewConfig.getBackgroundColor());
            buttonsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            buttonsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            mainPanel.add(buttonsScrollPane, "0, 1");
        } else {
            buttonsPanel.setLayout(new GridLayout(5, 2));
            mainPanel.add(buttonsPanel, "0,1");
        }

        for (NodePseudonym pseudonym : nodeManager.getCurrentNode().getChildren()) {
            JButton button = new JButton();
            button.setText(getHtmlText(pseudonym.getPhrase(), viewConfig.getButtonsFontSize()));
            button.addActionListener(e -> {
                nodeManager.setCurrentNode(pseudonym.getNode());
                lastNodesList.add(nodeManager.getCurrentNode());
                rollbackLastNodesList.clear();
                drawCurrentNode();
            });
            buttonsPanel.add(button);
        }

        mainPanel.revalidate();
        this.repaint();
    }

    private String getHtmlText(String text, Integer fontSize) {
        return "<html><body><p style='font-size:" + fontSize + "'>" + text + "</p></body></html>";
    }

}