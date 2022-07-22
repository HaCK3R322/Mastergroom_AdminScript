package com.androsov.gui;

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

class CenteredFrame extends JFrame {
    public CenteredFrame() {
        final int width = 800;
        final int height = 800;
        final boolean resizable = true;

        this.setSize(width, height);
        this.setResizable(resizable);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - width / 2, dim.height / 2 - height / 2);
    }

    public CenteredFrame(Integer width, Integer height, boolean resizable) {
        this.setSize(width, height);
        this.setResizable(resizable);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - width / 2, dim.height / 2 - height / 2);
    }
}

public class ScriptFrame {
    private final JFrame frame;
    private final JPanel panel;
    private final JMenuBar menuBar;
    private final Logger logger;
    private final NodeManager nodeManager;
    private Node currentNode;
    private final ArrayList<Node> lastNodes = new ArrayList<>();

    private Integer TEXT_FONT_SIZE = 20;
    private Integer BUTTONS_FONT_SIZE = 20;
    private Color BACKGROUND_COLOR = Color.WHITE;
    private final Integer SETTINGS_FRAME_SIZE_X = 500;
    private final Integer SETTINGS_FRAME_SIZE_Y = 500;

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
        int FRAME_SIZE_X = 800;
        int FRAME_SIZE_Y = 800;
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
        lastNodes.add(currentNode);
        drawCurrentNode();

        addGoToPreviousNodeEvent();

        frame.setVisible(true);
    }

    private void addGoToPreviousNodeEvent() {
        frame.setFocusable(true);

        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    goToPreviousNode();
                    drawCurrentNode();
                }
            }
        });
    }

    private void goToPreviousNode() {
        if (lastNodes.size() > 1) {
            currentNode = lastNodes.get(lastNodes.size() - 2);
            lastNodes.remove(lastNodes.size() - 1);
        }
    }

    private void drawCurrentNode() {
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
        // every click saves to lastNodes list
        for (NodePseudonym pseudonym : currentNode.getChildren()) {
            JButton button = new JButton();
            button.setText(getHtmlText(pseudonym.getPhrase(), BUTTONS_FONT_SIZE));
            button.addActionListener(e -> {
                currentNode = pseudonym.getNode();
                lastNodes.add(currentNode);
                drawCurrentNode();
            });
            buttonsPanel.add(button);
        }

        panel.revalidate();
        frame.repaint();
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

    private void configureMenuBarContent() {
        // add menu "Settings"
        JMenu settingsMenu = new JMenu("Настройки");
        settingsMenu.setBackground(Color.WHITE);
        menuBar.add(settingsMenu);

        // View
        JMenuItem settingsMenuItem = new JMenuItem("Вид");
        settingsMenuItem.addActionListener(e -> {
            new ViewSettingsFrame();
        });
        settingsMenu.add(settingsMenuItem);

        // Redactor
        JMenuItem redactorMenuItem = new JMenuItem("Редакитровать этот узел");
        redactorMenuItem.addActionListener(e -> {
            new RedactorFrame();
        });
        settingsMenu.add(redactorMenuItem);
    }

    private class RedactorFrame extends CenteredFrame {
        public RedactorFrame() {
            super(SETTINGS_FRAME_SIZE_X, SETTINGS_FRAME_SIZE_Y, false);

            JPanel redactorPanel = new JPanel();
            redactorPanel.setOpaque(true);
            this.setBackground(BACKGROUND_COLOR);
            redactorPanel.setLayout(new GridLayout(5, 1));
            this.add(redactorPanel);

            // button that opens phrase redactor frame
            JButton phraseRedactorButton = new JButton("Изменить фразу");
            phraseRedactorButton.addActionListener(e -> {
                new PhraseRedactorFrame();
            });
            redactorPanel.add(phraseRedactorButton);


            // button to open new frame to create new node and its pseudonym
            JPanel newNodePanel = new JPanel();
            newNodePanel.setLayout(new GridLayout(1, 2));
            newNodePanel.setOpaque(false);
            JButton newNodeButton = new JButton("Добавить новый узел");
            newNodeButton.addActionListener(e -> {
                new AddNewNodeFrame();
            });
            newNodePanel.add(newNodeButton);
            redactorPanel.add(newNodePanel);

            // button to open new frame to add existing node to current node
            JPanel addNodePanel = new JPanel();
            addNodePanel.setLayout(new GridLayout(1, 2));
            addNodePanel.setOpaque(false);
            JButton addNodeButton = new JButton("Добавить существующий узел");
            addNodeButton.addActionListener(e -> {
                new AddExistingNodeFrame();
            });
            addNodePanel.add(addNodeButton);
            redactorPanel.add(addNodePanel);

            // button to choose and delete child node from current node
            JPanel deleteChildNodePanel = new JPanel();
            deleteChildNodePanel.setLayout(new GridLayout(1, 2));
            deleteChildNodePanel.setOpaque(false);
            JButton deleteChildNodeButton = new JButton("Удалить дочерний узел");
            deleteChildNodeButton.addActionListener(e -> {
                new DeleteChildNodeFrame();
            });
            deleteChildNodePanel.add(deleteChildNodeButton);
            redactorPanel.add(deleteChildNodePanel);

            // button to delete current from all nodes children
            // then delete current node
            JPanel deleteNodePanel = new JPanel();
            deleteNodePanel.setLayout(new GridLayout(1, 2));
            deleteNodePanel.setOpaque(false);
            JButton deleteNodeButton = new JButton("Удалить этот узел");
            // show dialog to confirm deletion of node
            deleteNodeButton.addActionListener(e -> {
                int dialogResult = JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить этот узел?", "Удаление узла", JOptionPane.YES_NO_OPTION);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    deleteCurrentNode();
                    currentNode = nodeManager.getFirstNode();
                    drawCurrentNode();
                }
            });
            deleteNodePanel.add(deleteNodeButton);
            redactorPanel.add(deleteNodePanel);

            this.setVisible(true);
        }
    }

    private class ViewSettingsFrame extends CenteredFrame {
        // create new frame, that will contain settings and labels. Then, add labels and settings to it.
        public ViewSettingsFrame() {
            super(SETTINGS_FRAME_SIZE_X, SETTINGS_FRAME_SIZE_Y, false);

            JPanel settingsPanel = new JPanel();
            settingsPanel.setLayout(new GridLayout(4, 1));
            this.add(settingsPanel);

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
                this.dispose();
                drawCurrentNode();
            });
            settingsPanel.add(saveSettingsButton);

            this.setVisible(true);
        }
    }

    private class PhraseRedactorFrame extends CenteredFrame {
        public PhraseRedactorFrame() {
            super(SETTINGS_FRAME_SIZE_X, SETTINGS_FRAME_SIZE_Y, false);

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
    }

    private class AddNewNodeFrame extends CenteredFrame {
        public AddNewNodeFrame() {
            super(SETTINGS_FRAME_SIZE_X, SETTINGS_FRAME_SIZE_Y / 2, false);

            JPanel newNodePanel = new JPanel();
            newNodePanel.setLayout(new GridLayout(3, 1));
            this.add(newNodePanel);

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
                this.dispose();
                drawCurrentNode();
                saveNodesOrShowError();
            });
            saveNodePanel.add(saveNodeButton);
            newNodePanel.add(saveNodePanel);
            this.setVisible(true);
        }
    }

    private class AddExistingNodeFrame extends CenteredFrame {
        public AddExistingNodeFrame() {
            super(SETTINGS_FRAME_SIZE_X, SETTINGS_FRAME_SIZE_Y / 2, false);

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
                this.dispose();
                drawCurrentNode();
                saveNodesOrShowError();
            });

            saveNodePanel.add(saveNodeButton);
            newNodePanel.add(saveNodePanel);
            this.setVisible(true);
        }
    }

    private class DeleteChildNodeFrame extends CenteredFrame {
        public DeleteChildNodeFrame() {
            super(SETTINGS_FRAME_SIZE_X, SETTINGS_FRAME_SIZE_Y / 4, false);

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
                this.dispose();
                drawCurrentNode();
                saveNodesOrShowError();
            });

            saveNodePanel.add(saveNodeButton);
            deleteChildNodePanel.add(saveNodePanel);
            this.setVisible(true);
        }
    }
}