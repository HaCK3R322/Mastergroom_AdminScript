package com.androsov.gui.frames.settings;

import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.DefaultFrame;

import javax.swing.*;
import java.awt.*;

public class ViewSettingsFrame extends DefaultFrame {
    private static final ViewConfig viewConfig = ViewConfig.getInstance();

    // create new frame, that will contain settings and labels. Then, add labels and settings to it.
    public ViewSettingsFrame(DefaultFrame parent) {
        super(viewConfig.getSettingsFrameSizeX(), viewConfig.getSettingsFrameSizeY(), false);
        this.setTitle("Настройки вида");
        this.setBackground(viewConfig.getBackgroundColor());

        final int numberOfSettings = 5;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        this.add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel labelsPanel = new JPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = numberOfSettings;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(labelsPanel, gbc);
        labelsPanel.setLayout(new GridLayout(numberOfSettings, 1));

        JPanel settingsPanel = new JPanel();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 6;
        gbc.weighty = numberOfSettings;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(settingsPanel, gbc);
        settingsPanel.setLayout(new GridLayout(numberOfSettings, 1));
        // set border for settings panel
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // text font setting by slider from 10 to 50
        JLabel textFontSizeLabel = new JLabel("Размер текста", SwingConstants.LEFT);
        textFontSizeLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        JSlider textFontSizeSlider = new JSlider(10, 50, viewConfig.getTextFontSize());
        textFontSizeSlider.setMajorTickSpacing(10);
        textFontSizeSlider.setMinorTickSpacing(1);
        textFontSizeSlider.setPaintTicks(true);
        textFontSizeSlider.setPaintLabels(true);
        textFontSizeSlider.setSnapToTicks(true);
        textFontSizeSlider.addChangeListener(e -> {
            viewConfig.setTextFontSize(textFontSizeSlider.getValue());
            parent.refresh();
        });
        labelsPanel.add(textFontSizeLabel);
        settingsPanel.add(textFontSizeSlider);

        // buttons font setting by slider from 10 to 50
        JLabel buttonsFontSizeLabel = new JLabel("Размер текста кнопок", SwingConstants.LEFT);
        buttonsFontSizeLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        JSlider buttonsFontSizeSlider = new JSlider(10, 50, viewConfig.getButtonsFontSize());
        buttonsFontSizeSlider.setMajorTickSpacing(10);
        buttonsFontSizeSlider.setMinorTickSpacing(1);
        buttonsFontSizeSlider.setPaintTicks(true);
        buttonsFontSizeSlider.setPaintLabels(true);
        buttonsFontSizeSlider.setSnapToTicks(true);
        buttonsFontSizeSlider.addChangeListener(e -> {
            viewConfig.setButtonsFontSize(buttonsFontSizeSlider.getValue());
            parent.refresh();
        });
        labelsPanel.add(buttonsFontSizeLabel);
        settingsPanel.add(buttonsFontSizeSlider);

        // background color setting by radio buttons in button group
        JLabel backgroundColorLabel = new JLabel("Цвет фона", SwingConstants.LEFT);
        backgroundColorLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        // radio group
        ButtonGroup backgroundColorGroup = new ButtonGroup();
        JRadioButton backgroundColorWhite = new JRadioButton("Белый");
        JRadioButton backgroundColorLightGray = new JRadioButton("Светло-серый");
        JRadioButton backgroundColorGray = new JRadioButton("Серый");
        backgroundColorGroup.add(backgroundColorWhite);
        backgroundColorGroup.add(backgroundColorLightGray);
        backgroundColorGroup.add(backgroundColorGray);
        // panel for buttons
        JPanel backgroundColorButtonsPanel = new JPanel();
        backgroundColorButtonsPanel.setBackground(Color.WHITE);
        backgroundColorButtonsPanel.setLayout(new GridLayout(3, 1));
        backgroundColorButtonsPanel.add(backgroundColorWhite);
        backgroundColorButtonsPanel.add(backgroundColorLightGray);
        backgroundColorButtonsPanel.add(backgroundColorGray);
        // for each button add listener that will set background color of panel to button color
        // set selected button according to current background color
        backgroundColorWhite.addActionListener(e -> {viewConfig.setBackgroundColor(Color.WHITE); parent.refresh();});
        backgroundColorLightGray.addActionListener(e -> {viewConfig.setBackgroundColor(Color.LIGHT_GRAY); parent.refresh();});
        backgroundColorGray.addActionListener(e -> {viewConfig.setBackgroundColor(Color.GRAY); parent.refresh();});
        if (viewConfig.getBackgroundColor().equals(Color.WHITE)) {
            backgroundColorWhite.setSelected(true);
        } else if (viewConfig.getBackgroundColor().equals(Color.LIGHT_GRAY)) {
            backgroundColorLightGray.setSelected(true);
        } else if (viewConfig.getBackgroundColor().equals(Color.GRAY)) {
            backgroundColorGray.setSelected(true);
        }
        labelsPanel.add(backgroundColorLabel);
        settingsPanel.add(backgroundColorButtonsPanel);


        // show help check
        JLabel showHelpLabel = new JLabel("Справка", SwingConstants.LEFT);
        showHelpLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 30, 0));
        JCheckBox showHelpCheck = new JCheckBox("Отображать на страничке, если имеется");
        showHelpCheck.setSelected(viewConfig.getShowHelp());
        showHelpCheck.addActionListener(e -> {
            viewConfig.setShowHelp(showHelpCheck.isSelected());
            parent.refresh();
        });
        JPanel showHelpCheckPanel = new JPanel();
        showHelpCheckPanel.setLayout(new GridLayout(1, 1));
        showHelpCheckPanel.setBackground(Color.WHITE);
        labelsPanel.add(showHelpLabel);
        showHelpCheckPanel.add(showHelpCheck);
        settingsPanel.add(showHelpCheckPanel);

        // save pos check
        JLabel savePosLabel = new JLabel("Позиция", SwingConstants.LEFT);
        savePosLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 30, 0));
        JCheckBox savePosCheckBox = new JCheckBox("Сохранять при закрыти программы");
        savePosCheckBox.setSelected(viewConfig.getSavePos());
        savePosCheckBox.addActionListener(e -> {
            viewConfig.setSavePos(savePosCheckBox.isSelected());
            parent.refresh();
        });
        JPanel savePosCheckPanel = new JPanel();
        savePosCheckPanel.setLayout(new GridLayout(1, 1));
        savePosCheckPanel.setBackground(Color.WHITE);
        labelsPanel.add(savePosLabel);
        savePosCheckPanel.add(savePosCheckBox);
        settingsPanel.add(savePosCheckPanel);

        // button to save settings
        JButton saveSettingsButton = new JButton("Сохранить");
        saveSettingsButton.addActionListener(e -> {
            viewConfig.saveConfig();
            parent.refresh();
            this.dispose();
        });
        gbc.gridx = 0;
        gbc.gridy = numberOfSettings;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridwidth = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(saveSettingsButton, gbc);

        // for all components set background color to white
        for (Component component : mainPanel.getComponents()) {
            component.setBackground(Color.WHITE);
        }

        this.setVisible(true);
    }
}
