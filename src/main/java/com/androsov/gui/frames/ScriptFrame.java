package com.androsov.gui.frames;

import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.help.HelpFrame;
import com.androsov.gui.frames.help.HelpManager;
import com.androsov.gui.frames.help.HelpMenu;
import com.androsov.gui.frames.help.HelpRedactorFrame;
import com.androsov.gui.frames.search.SearchFrame;
import com.androsov.gui.frames.search.SearchPanel;
import com.androsov.gui.frames.settings.RedactorFrame;
import com.androsov.gui.frames.settings.view.SeparateHelpCheckBoxMenuItem;
import com.androsov.gui.frames.settings.view.ShowHelpCheckBoxMenuItem;
import com.androsov.gui.frames.settings.view.ShowSearchPanelCheckBoxMenuItem;
import com.androsov.gui.frames.settings.view.ViewSettingsFrame;
import com.androsov.node.Node;
import com.androsov.node.NodeManager;
import com.androsov.node.NodePseudonym;
import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
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

        nodeManager.setCurrentNode(nodeManager.getFirstNode());
        lastNodesList.add(nodeManager.getCurrentNode());
        renderCurrentNode();

        addNodeKeyMovementsToFrame();

        this.setVisible(true);

        // on exit save settings
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Logger.getLogger("AdminScriptLogger").info("Script frame closing event was triggered.");
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
                    renderCurrentNode();
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    goCloserToLastClickedNode();
                    renderCurrentNode();
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    goToStartNode();
                    renderCurrentNode();
                }
            }
        });
    }

    private void reconfigureMenuBar() {
        menuBar.removeAll();
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
            renderCurrentNode();
        });
        searchMenu.add(goToStartItem);
        menuBar.add(searchMenu);

        // View
        JMenu viewSettingsMenu = new JMenu("Вид");
        settingsMenu.add(viewSettingsMenu);
        JMenuItem openSettings = new JMenuItem("Открыть настройки вида");
        openSettings.addActionListener(e -> new ViewSettingsFrame(this));
        viewSettingsMenu.add(openSettings);
        viewSettingsMenu.add(new JSeparator());
        viewSettingsMenu.add(new ShowSearchPanelCheckBoxMenuItem(this, "Отображать панель поиска"));
        viewSettingsMenu.add(new ShowHelpCheckBoxMenuItem(this, "Показывать подсказки"));
        viewSettingsMenu.add(new SeparateHelpCheckBoxMenuItem(this, "Разделять подсказки по \"" + HelpManager.getSeparator() + "\""));

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
        // if node was changed, save it to last nodes list
        if (!nodeManager.getCurrentNode().equals(lastNodesList.get(lastNodesList.size() - 1))) {
            lastNodesList.add(nodeManager.getCurrentNode());
        }

        renderCurrentNode();
    }

    private void addToMainPanelWithTableLayoutConstraints(Component component, int x, int y) {
        mainPanel.add(component, x + "," + y);
    }
    private void renderCurrentNode() { // render current node
        mainPanel.removeAll();

        this.setBackground(viewConfig.getBackgroundColor());

        double[][] mainPanelSize = getTableSize();
        mainPanel.setLayout(new TableLayout(mainPanelSize));
        int currentRow = 0;

        if (viewConfig.getShowSearch()) {
            addToMainPanelWithTableLayoutConstraints(new SearchPanel(this), 0, currentRow);
            currentRow++;
        }

        JLabel phraseLabel = new JLabel();
        phraseLabel.setText(getHtmlText(nodeManager.getCurrentNode().getPhrase(), viewConfig.getTextFontSize()));
        phraseLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel phrasePanel = new JPanel();
        phrasePanel.setBackground(viewConfig.getBackgroundColor());

        if(HelpManager.isHelpExists(nodeManager.getCurrentNode().getId()) && viewConfig.getShowHelp()) { // if help exists, and show help is true
            HelpManager.HelpNode helpNode = HelpManager.getHelpMap().get(nodeManager.getCurrentNode().getId());
            String text = helpNode.getHelpTopic() + ":\n" + helpNode.getHelpText();

            if (text.contains(HelpManager.getSeparator()) && viewConfig.getSeparateHelp()) {                                                                            // if
                int n = text.split(HelpManager.getSeparator()).length;
                n += 1; // add 1 for phrase
                int total = n * 2 - 1;
                double[][] phrasePanelTableSizes = new double[][]{
                        new double[total], // for space between phrases
                        new double[]{
                                TableLayout.FILL
                        }
                };
                phrasePanelTableSizes[0][0] = TableLayout.FILL;
                for (int i = 1; i < total; i += 2) {
                    phrasePanelTableSizes[0][i] = 0.01;
                    phrasePanelTableSizes[0][i + 1] = 1.0 / n;
                }
                phrasePanel.setLayout(new TableLayout(phrasePanelTableSizes));

                for (int i = 0; i < n; i++) {
                    if(i == 0) {
                        phrasePanel.add(phraseLabel, i + ",0");
                        continue;
                    }

                    String helpText = text.split(HelpManager.getSeparator())[i - 1]; // get separated help text

                    JScrollPane helpTextScrollPane = getScrollPaneWithText(helpText);
                    helpTextScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                    helpTextScrollPane.getHorizontalScrollBar().setUnitIncrement(4);

                    phrasePanel.add(helpTextScrollPane, (i*2) + ",0");
                }
            } else { // if we don't separate help by separator or text doesn't contain separator
                text = text.replaceAll(HelpManager.getSeparator(), "\n\n");

                phrasePanel.setLayout(new TableLayout(new double[][] {
                        {goldenRatio, TableLayout.FILL},
                        {TableLayout.FILL}
                }));
                phrasePanel.add(phraseLabel, "0, 0");

                JScrollPane helpTextScrollPane = getScrollPaneWithText(text);
                helpTextScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                helpTextScrollPane.getHorizontalScrollBar().setUnitIncrement(4);

                phrasePanel.add(helpTextScrollPane, "1, 0");
            }
        } else {
            phrasePanel.setLayout(new GridLayout(1, 1));
            phrasePanel.add(phraseLabel);
        }


        addToMainPanelWithTableLayoutConstraints(phrasePanel, 0, currentRow);
        currentRow++;

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
            buttonsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            buttonsScrollPane.getVerticalScrollBar().setUnitIncrement(16);

            addToMainPanelWithTableLayoutConstraints(buttonsScrollPane, 0, currentRow);
        } else {
            buttonsPanel.setLayout(new GridLayout(5, 2));
            addToMainPanelWithTableLayoutConstraints(buttonsPanel, 0, currentRow);
        }

        for (NodePseudonym pseudonym : nodeManager.getCurrentNode().getChildren()) {
            JButton button = new JButton();
            button.setText(getHtmlText(pseudonym.getPhrase(), viewConfig.getButtonsFontSize()));
            button.addActionListener(e -> {
                nodeManager.setCurrentNode(pseudonym.getNode());
                lastNodesList.add(nodeManager.getCurrentNode());
                rollbackLastNodesList.clear();
                renderCurrentNode();
            });
            buttonsPanel.add(button);
        }

        mainPanel.revalidate();
        reconfigureMenuBar();
        this.repaint();
    }

    private double[][] getTableSize() {
        if(viewConfig.getShowSearch()) {
            final double searchPanelHeight = 0.05;
            return new double[][]{{TableLayout.FILL},{searchPanelHeight, TableLayout.FILL, goldenRatio}};
        } else {
            return new double[][]{{TableLayout.FILL},{TableLayout.FILL, goldenRatio}};
        }
    }

    private String getHtmlText(String text, Integer fontSize) {
        return "<html><body><p style='font-size:" + fontSize + "'>" + text + "</p></body></html>";
    }

    private JScrollPane getScrollPaneWithText(String text) {
        JTextArea textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setText(text);
        textArea.setFont(new Font("Default", Font.ITALIC, (int)(viewConfig.getTextFontSize() * goldenRatio)));
        textArea.setEditable(false);
        textArea.setBackground(viewConfig.getBackgroundColor());
        textArea.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBackground(viewConfig.getBackgroundColor());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        return scrollPane;
    }
}