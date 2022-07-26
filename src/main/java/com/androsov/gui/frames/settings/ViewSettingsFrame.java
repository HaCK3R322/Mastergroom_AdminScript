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

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(4, 1));
        this.add(settingsPanel);

        // text font setting by slider from 10 to 50
        JPanel textFontSizePanel = new JPanel();
        textFontSizePanel.setLayout(new GridLayout(1, 2));
        JLabel textFontSizeLabel = new JLabel();
        textFontSizeLabel.setText("Размер текста");
        textFontSizePanel.add(textFontSizeLabel);
        JSlider textFontSizeSlider = new JSlider(10, 50, viewConfig.getTextFontSize());
        textFontSizeSlider.setMajorTickSpacing(10);
        textFontSizeSlider.setMinorTickSpacing(1);
        textFontSizeSlider.setPaintTicks(true);
        textFontSizeSlider.setPaintLabels(true);
        textFontSizeSlider.setSnapToTicks(true);
        textFontSizePanel.add(textFontSizeSlider);
        textFontSizeSlider.addChangeListener(e -> {
            viewConfig.setTextFontSize(textFontSizeSlider.getValue());
            parent.refresh();
        });
        settingsPanel.add(textFontSizePanel);

        // buttons font setting by slider from 10 to 50
        JPanel buttonsFontSizePanel = new JPanel();
        buttonsFontSizePanel.setLayout(new GridLayout(1, 2));
        JLabel buttonsFontSizeLabel = new JLabel();
        buttonsFontSizeLabel.setText("Размер кнопок");
        buttonsFontSizePanel.add(buttonsFontSizeLabel);
        JSlider buttonsFontSizeSlider = new JSlider(10, 50, viewConfig.getButtonsFontSize());
        buttonsFontSizeSlider.setMajorTickSpacing(10);
        buttonsFontSizeSlider.setMinorTickSpacing(1);
        buttonsFontSizeSlider.setPaintTicks(true);
        buttonsFontSizeSlider.setPaintLabels(true);
        buttonsFontSizeSlider.setSnapToTicks(true);
        buttonsFontSizePanel.add(buttonsFontSizeSlider);
        buttonsFontSizeSlider.addChangeListener(e -> {
            viewConfig.setButtonsFontSize(buttonsFontSizeSlider.getValue());
            parent.refresh();
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
                viewConfig.setBackgroundColor(Color.WHITE);
            } else if (backgroundColorLightGray.isSelected()) {
                viewConfig.setBackgroundColor(Color.LIGHT_GRAY);
            } else if (backgroundColorGray.isSelected()) {
                viewConfig.setBackgroundColor(Color.GRAY);
            }
            this.dispose();
            parent.refresh();
            viewConfig.saveConfig();
        });
        settingsPanel.add(saveSettingsButton);

        this.setVisible(true);
    }
}
