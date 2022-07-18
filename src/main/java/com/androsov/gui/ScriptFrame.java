package com.androsov.gui;

import com.androsov.LoggerConfigurer;
import com.androsov.node.Node;
import com.androsov.node.NodePseudonym;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;

public class ScriptFrame {
    JFrame frame;
    JPanel panel;
    Node startNode;
    Logger logger;

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

    public ScriptFrame(Node startNode) {
        logger = LoggerConfigurer.getConfiguredLogger(ScriptFrame.class.getName());

        this.startNode = startNode;

        frame = new JFrame();
        frame.setSize(FRAME_SIZE_X, FRAME_SIZE_Y);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

        panel = new JPanel();
        frame.add(panel);

        drawNode(startNode);

        // add menu bar that opens settings and is white and has black border at the bottom
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        frame.setJMenuBar(menuBar);

        // add menu "Settings"
        JMenu settingsMenu = new JMenu("Настройки");
        settingsMenu.setBackground(Color.WHITE);
        menuBar.add(settingsMenu);

        // add menu item "Settings"
        JMenuItem settingsMenuItem = new JMenuItem("Вид");
        settingsMenuItem.addActionListener(e -> {
            openSettingsFrame();
        });
        settingsMenu.add(settingsMenuItem);


        frame.setVisible(true);
    }

    public void drawNode(Node node) {
        panel.removeAll();

        panel.setLayout(new GridLayout(1, 2));

        JPanel phrasePanel = new JPanel();
        phrasePanel.setLayout(new GridLayout(1, 1));
        JLabel phraseLabel = new JLabel();
        phrasePanel.add(phraseLabel);
        phraseLabel.setText(getHtmlText(node.getPhrase(), TEXT_FONT_SIZE));
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
        for (NodePseudonym pseudonym : node.getChildren()) {
            JButton button = new JButton();
            button.setText(getHtmlText(pseudonym.getPhrase(), BUTTONS_FONT_SIZE));
            button.addActionListener(e -> {
                drawNode(pseudonym.getNode());
            });
            buttonsPanel.add(button);
        }

        panel.revalidate();
        frame.repaint();
    }

    private String getHtmlText(String text, Integer fontSize) {
        return "<html><p style='font-size:" + fontSize + "'>" + text + "</p></html>";
    }

    private void openSettingsFrame() {
        // create new frame, that will contain settings and labels. Then, add labels and settings to it.
        JFrame settingsFrame = new JFrame();
        settingsFrame.setSize(SETTINGS_FRAME_SIZE_X, SETTINGS_FRAME_SIZE_Y);
        settingsFrame.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        settingsFrame.setLocation(dim.width / 2 - settingsFrame.getSize().width / 2, dim.height / 2 - settingsFrame.getSize().height / 2);

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
            drawNode(startNode);
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
            drawNode(startNode);
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
            drawNode(startNode);
        });
        settingsPanel.add(saveSettingsButton);

        settingsFrame.setVisible(true);
    }
}