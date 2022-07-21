package com.androsov.gui;

import com.androsov.node.Node;
import com.androsov.node.NodeManager;
import com.androsov.node.NodePseudonym;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScriptFrame {
    JFrame frame;
    JPanel panel;
    JMenuBar menuBar;
    Logger logger;
    NodeManager nodeManager;
    Node currentNode;

    private Integer TEXT_FONT_SIZE = 20;
    private Integer BUTTONS_FONT_SIZE = 20;
    private Color BACKGROUND_COLOR = Color.WHITE;
    private Integer FRAME_SIZE_X = 800;
    private Integer FRAME_SIZE_Y = 800;
    private Integer SETTINGS_FRAME_SIZE_X = 500;
    private Integer SETTINGS_FRAME_SIZE_Y = 500;

    public void setTextFontSize(Integer textFontSize) {
        this.TEXT_FONT_SIZE = textFontSize;
    }
    public void setButtonsFontSize(Integer buttonsFontSize) {
        this.BUTTONS_FONT_SIZE = buttonsFontSize;
    }
    public void setBackgroundColor(Color backgroundColor) {
        this.BACKGROUND_COLOR = backgroundColor;
    }

    public ScriptFrame() {
        logger = Logger.getLogger("AdminScriptLogger");
        nodeManager = NodeManager.getInstance();

        frame = new JFrame();
        frame.setSize(FRAME_SIZE_X, FRAME_SIZE_Y);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel();
        frame.add(panel);

        // add menu bar to frame
        menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        frame.setJMenuBar(menuBar);
        configureMenuBarContent();

        currentNode = nodeManager.getFirstNode();
        drawCurrentNode();

        frame.setVisible(true);
    }

    public void drawCurrentNode() {
        panel.removeAll();

        panel.setLayout(new GridLayout(1, 2));

        JPanel phrasePanel = new JPanel();
        phrasePanel.setLayout(new GridLayout(1, 1));
        JLabel phraseLabel = new JLabel();
        phrasePanel.add(phraseLabel);
        phraseLabel.setText(getHtmlText(currentNode.getPhrase(), TEXT_FONT_SIZE));
        phraseLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(phrasePanel);
        phraseLabel.setOpaque(true);
        phraseLabel.setBackground(BACKGROUND_COLOR);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(10, 1));
        panel.add(buttonsPanel);
        buttonsPanel.setOpaque(true);
        buttonsPanel.setBackground(BACKGROUND_COLOR);

        // buttons to connect to other nodes
        for (NodePseudonym pseudonym : currentNode.getChildren()) {
            JButton button = new JButton();
            button.setText(getHtmlText(pseudonym.getPhrase(), BUTTONS_FONT_SIZE));
            button.addActionListener(e -> {
                currentNode = pseudonym.getNode();
                drawCurrentNode();
            });
            buttonsPanel.add(button);
        }

        panel.revalidate();
        frame.repaint();
    }

    private void configureMenuBarContent() {
        // add menu "Settings"
        JMenu settingsMenu = new JMenu("Настройки");
        settingsMenu.setBackground(Color.WHITE);
        menuBar.add(settingsMenu);

        // View
        JMenuItem settingsMenuItem = new JMenuItem("Вид");
        settingsMenuItem.addActionListener(e -> {
            openViewSettingsFrame();
        });
        settingsMenu.add(settingsMenuItem);

        // Redactor
        JMenuItem redactorMenuItem = new JMenuItem("Редакитровать этот узел");
        redactorMenuItem.addActionListener(e -> {
            openRedactorFrame();
        });
        settingsMenu.add(redactorMenuItem);
    }

    private void openViewSettingsFrame() {
        // create new frame, that will contain settings and labels. Then, add labels and settings to it.
        JFrame settingsFrame = createNewCenteredFrame(SETTINGS_FRAME_SIZE_X, SETTINGS_FRAME_SIZE_Y, false);

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(4, 1));
        settingsFrame.add(settingsPanel);

        // text font setting by slider from 10 to 50
        JPanel textFontSizePanel = new JPanel();
        textFontSizePanel.setLayout(new GridLayout(1, 2));
        JLabel textFontSizeLabel = new JLabel();
        textFontSizeLabel.setText("Размер текста");
        textFontSizePanel.add(textFontSizeLabel);
        JSlider textFontSizeSlider = new JSlider(10, 50, TEXT_FONT_SIZE);
        textFontSizeSlider.setMajorTickSpacing(10);
        textFontSizeSlider.setMinorTickSpacing(1);
        textFontSizeSlider.setPaintTicks(true);
        textFontSizeSlider.setPaintLabels(true);
        textFontSizeSlider.setSnapToTicks(true);
        textFontSizePanel.add(textFontSizeSlider);
        textFontSizeSlider.addChangeListener(e -> {
            setTextFontSize(textFontSizeSlider.getValue());
            drawCurrentNode();
        });
        settingsPanel.add(textFontSizePanel);

        // buttons font setting by slider from 10 to 50
        JPanel buttonsFontSizePanel = new JPanel();
        buttonsFontSizePanel.setLayout(new GridLayout(1, 2));
        JLabel buttonsFontSizeLabel = new JLabel();
        buttonsFontSizeLabel.setText("Размер кнопок");
        buttonsFontSizePanel.add(buttonsFontSizeLabel);
        JSlider buttonsFontSizeSlider = new JSlider(10, 50, BUTTONS_FONT_SIZE);
        buttonsFontSizeSlider.setMajorTickSpacing(10);
        buttonsFontSizeSlider.setMinorTickSpacing(1);
        buttonsFontSizeSlider.setPaintTicks(true);
        buttonsFontSizeSlider.setPaintLabels(true);
        buttonsFontSizeSlider.setSnapToTicks(true);
        buttonsFontSizePanel.add(buttonsFontSizeSlider);
        buttonsFontSizeSlider.addChangeListener(e -> {
            setButtonsFontSize(buttonsFontSizeSlider.getValue());
            drawCurrentNode();
        });
        settingsPanel.add(buttonsFontSizePanel);

        // background color setting by radio buttons in button group
        JPanel backgroundColorPanel = new JPanel(); // panel
        backgroundColorPanel.setLayout(new GridLayout(1, 2)); // layout
        JLabel backgroundColorLabel = new JLabel("Цвет фона"); // label
        ButtonGroup backgroundColorGroup = new ButtonGroup(); // button group
        JRadioButton backgroundColorWhite = new JRadioButton("Белый"); // white button
        JRadioButton backgroundColorLightGray = new JRadioButton("Светло-серый"); // light gray button
        JRadioButton backgroundColorGray = new JRadioButton("Серый"); // black button
        backgroundColorGroup.add(backgroundColorWhite); // add white button to group
        backgroundColorGroup.add(backgroundColorLightGray); // add light gray button to group
        backgroundColorGroup.add(backgroundColorGray); // add black button to group
        backgroundColorPanel.add(backgroundColorLabel); // add label to panel
        backgroundColorPanel.add(backgroundColorWhite); // add white button to panel
        backgroundColorPanel.add(backgroundColorLightGray); // add light gray button to panel
        backgroundColorPanel.add(backgroundColorGray); // add black button to panel
        settingsPanel.add(backgroundColorPanel);

        // button to save settings
        JButton saveSettingsButton = new JButton("Сохранить"); // button
        saveSettingsButton.addActionListener(e -> {
            if (backgroundColorWhite.isSelected()) {
                BACKGROUND_COLOR = Color.WHITE;
            } else if (backgroundColorLightGray.isSelected()) {
                BACKGROUND_COLOR = Color.LIGHT_GRAY;
            } else if (backgroundColorGray.isSelected()) {
                BACKGROUND_COLOR = Color.GRAY;
            }
            settingsFrame.dispose();
            drawCurrentNode();
        });
        settingsPanel.add(saveSettingsButton);

        settingsFrame.setVisible(true);
    }

    private void openRedactorFrame() {
        JFrame redactorFrame = new JFrame();
        redactorFrame.setSize(SETTINGS_FRAME_SIZE_X, SETTINGS_FRAME_SIZE_Y);
        redactorFrame.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        redactorFrame.setLocation(dim.width / 2 - redactorFrame.getSize().width / 2, dim.height / 2 - redactorFrame.getSize().height / 2);

        JPanel redactorPanel = new JPanel();
        redactorPanel.setOpaque(true);
        redactorFrame.setBackground(BACKGROUND_COLOR);
        redactorPanel.setLayout(new GridLayout(5, 1));
        redactorFrame.add(redactorPanel);

        // button that opens phrase redactor frame
        JButton phraseRedactorButton = new JButton("Изменить фразу");
        phraseRedactorButton.addActionListener(e -> {
            openPhraseRedactorFrame();
        });
        redactorPanel.add(phraseRedactorButton);


        // button to open new frame to create new node and its pseudonym
        JPanel newNodePanel = new JPanel();
        newNodePanel.setLayout(new GridLayout(1, 2));
        newNodePanel.setOpaque(false);
        JButton newNodeButton = new JButton("Добавить новый узел");
        newNodeButton.addActionListener(e -> {
            openAddNewNodeFrame();
        });
        newNodePanel.add(newNodeButton);
        redactorPanel.add(newNodePanel);

        // button to open new frame to add existing node to current node
        JPanel addNodePanel = new JPanel();
        addNodePanel.setLayout(new GridLayout(1, 2));
        addNodePanel.setOpaque(false);
        JButton addNodeButton = new JButton("Добавить существующий узел");
        addNodeButton.addActionListener(e -> {
            openAddExistingNodeFrame();
        });
        addNodePanel.add(addNodeButton);
        redactorPanel.add(addNodePanel);

        // button to delete current from all nodes children
        // then delete current node
        JPanel deleteNodePanel = new JPanel();
        deleteNodePanel.setLayout(new GridLayout(1, 2));
        deleteNodePanel.setOpaque(false);
        JButton deleteNodeButton = new JButton("Удалить этот узел");
        // show dialog to confirm deletion of node
        deleteNodeButton.addActionListener(e -> {
            int dialogResult = JOptionPane.showConfirmDialog(redactorFrame, "Вы действительно хотите удалить этот узел?", "Удаление узла", JOptionPane.YES_NO_OPTION);
            if (dialogResult == JOptionPane.YES_OPTION) {
                deleteCurrentNode();
                currentNode = nodeManager.getFirstNode();
                drawCurrentNode();
            }
        });
        deleteNodePanel.add(deleteNodeButton);
        redactorPanel.add(deleteNodePanel);

        // button to choose and delete child node from current node
        JPanel deleteChildNodePanel = new JPanel();
        deleteChildNodePanel.setLayout(new GridLayout(1, 2));
        deleteChildNodePanel.setOpaque(false);
        JButton deleteChildNodeButton = new JButton("Удалить дочерний узел");
        deleteChildNodeButton.addActionListener(e -> {
            openDeleteChildNodeFrame();
        });
        deleteChildNodePanel.add(deleteChildNodeButton);
        redactorPanel.add(deleteChildNodePanel);

        redactorFrame.setVisible(true);
    }

    private void openPhraseRedactorFrame() {
        JFrame phraseRedactorFrame = new JFrame();
        phraseRedactorFrame.setSize(SETTINGS_FRAME_SIZE_X, SETTINGS_FRAME_SIZE_Y / 2);
        phraseRedactorFrame.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        phraseRedactorFrame.setLocation(dim.width / 2 - phraseRedactorFrame.getSize().width / 2, dim.height / 2 - phraseRedactorFrame.getSize().height / 2);

        JPanel phraseRedactorPanel = new JPanel();
        phraseRedactorPanel.setOpaque(true);
        phraseRedactorFrame.setBackground(BACKGROUND_COLOR);
        phraseRedactorPanel.setLayout(new GridLayout(2, 1));
        phraseRedactorFrame.add(phraseRedactorPanel);

        // text filed for phrase
        JTextField phraseTextField = new JTextField();
        phraseTextField.setText(currentNode.getPhrase());
        phraseRedactorPanel.add(phraseTextField);

        // button to save phrase
        JPanel savePhrasePanel = new JPanel();
        savePhrasePanel.setLayout(new GridLayout(1, 1));
        savePhrasePanel.setOpaque(false);
        JButton savePhraseButton = new JButton("Сохранить");
        savePhraseButton.addActionListener(e -> {
            currentNode.setPhrase(phraseTextField.getText());
            saveNodesOrShowError();
            phraseRedactorFrame.dispose();
            drawCurrentNode();
        });
        savePhrasePanel.add(savePhraseButton);
        phraseRedactorPanel.add(savePhrasePanel);
        phraseRedactorFrame.setVisible(true);
    }

    private void openAddNewNodeFrame() {
        JFrame newNodeFrame = createNewCenteredFrame(SETTINGS_FRAME_SIZE_X, SETTINGS_FRAME_SIZE_Y, false);

        JPanel newNodePanel = new JPanel();
        newNodePanel.setLayout(new GridLayout(3, 1));
        newNodeFrame.add(newNodePanel);

        // text field for node's pseudonym
        JPanel pseudonymPanel = new JPanel();
        pseudonymPanel.setLayout(new GridLayout(1, 2));
        pseudonymPanel.setOpaque(false);
        JLabel pseudonymLabel = new JLabel();
        pseudonymLabel.setText("Псевдоним");
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
            currentNode.addChild(newNode, pseudonymTextField.getText());
            newNodeFrame.dispose();
            drawCurrentNode();
            saveNodesOrShowError();
        });
        saveNodePanel.add(saveNodeButton);
        newNodePanel.add(saveNodePanel);
        newNodeFrame.setVisible(true);
    }

    private void openAddExistingNodeFrame() {
        JFrame newNodeFrame = createNewCenteredFrame(SETTINGS_FRAME_SIZE_X, SETTINGS_FRAME_SIZE_Y, false);

        JPanel newNodePanel = new JPanel();
        newNodePanel.setLayout(new GridLayout(3, 1));
        newNodeFrame.add(newNodePanel);

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
        pseudonymLabel.setText("Псевдоним");
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
        nodesListLabel.setText("Выберите узел");
        nodesListPanel.add(nodesListLabel);
        JComboBox<String> nodesListComboBox = new JComboBox<>();
        HashMap<String, Node> nodesMap = new HashMap<>();
        for (Node node : nodeManager.getNodes()) {
            nodesListComboBox.addItem(node.getPhrase());
            nodesMap.put(node.getPhrase(), node);
        }
        nodesListPanel.add(nodesListComboBox);
        newNodePanel.add(nodesListPanel);

        // button to save new node
        JPanel saveNodePanel = new JPanel();
        saveNodePanel.setLayout(new GridLayout(1, 2));
        saveNodePanel.setOpaque(false);
        JButton saveNodeButton = new JButton("Сохранить");
        saveNodeButton.addActionListener(e1 -> {
            currentNode.addChild(nodesMap.get(nodesListComboBox.getSelectedItem().toString()), pseudonymTextField.getText());
            newNodeFrame.dispose();
            drawCurrentNode();
            saveNodesOrShowError();
        });

        saveNodePanel.add(saveNodeButton);
        newNodePanel.add(saveNodePanel);
        newNodeFrame.setVisible(true);
    }

    private void openDeleteChildNodeFrame() {
        JFrame deleteChildNodeFrame = createNewCenteredFrame(SETTINGS_FRAME_SIZE_X, SETTINGS_FRAME_SIZE_Y, false);

        JPanel deleteChildNodePanel = new JPanel();
        deleteChildNodePanel.setLayout(new GridLayout(2, 1));
        deleteChildNodeFrame.add(deleteChildNodePanel);

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
        for (NodePseudonym nodePseudonym : currentNode.getChildren()) {
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
            currentNode.removeChild(nodesPseudonymsMap.get(nodesListComboBox.getSelectedItem().toString()));
            deleteChildNodeFrame.dispose();
            drawCurrentNode();
            saveNodesOrShowError();
        });

        saveNodePanel.add(saveNodeButton);
        deleteChildNodePanel.add(saveNodePanel);
        deleteChildNodeFrame.setVisible(true);
    }

    private void deleteCurrentNode() {
        nodeManager.removeNode(currentNode);
        saveNodesOrShowError();
    }

    private void saveNodesOrShowError() {
        try {
            nodeManager.saveNodes();
        } catch (IOException ex) {
            logger.log(Level.WARNING, "Could not save nodes: " + ex.getMessage());
            new ErrorMessageFrame("Could not save nodes: " + ex.getMessage());
        }
    }

    private String getHtmlText(String text, Integer fontSize) {
        return "<html><p style='font-size:" + fontSize + "'>" + text + "</p></html>";
    }

    private JFrame createNewCenteredFrame(Integer width, Integer height, boolean resizable) {
        JFrame frame = new JFrame();
        frame.setSize(width, height);
        frame.setResizable(resizable);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
        return frame;
    }
}