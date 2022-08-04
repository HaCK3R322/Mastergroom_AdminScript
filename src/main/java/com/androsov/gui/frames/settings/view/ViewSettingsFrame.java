package com.androsov.gui.frames.settings.view;

import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.DefaultFrame;
import com.androsov.gui.frames.EscClosableDefaultFrame;
import com.androsov.gui.frames.help.HelpManager;
import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;

public class ViewSettingsFrame extends EscClosableDefaultFrame {
    private static final ViewConfig viewConfig = ViewConfig.getInstance();

    // create new frame, that will contain settings and labels. Then, add labels and settings to it.
    public ViewSettingsFrame(DefaultFrame parent) {
        super(viewConfig.getSettingsFrameSizeX(), viewConfig.getSettingsFrameSizeY(), false);
        this.setTitle("Настройки вида");
        this.setBackground(viewConfig.getBackgroundColor());

        final int numberOfSettings = 6;
        final double spaceForObject = (double) 1 / (numberOfSettings + 1); // settings + save button
        // number of space of objects must be equal to number of settings + 1
        final double[][] size = {{0.33, TableLayout.FILL},
                {
                spaceForObject,
                spaceForObject,
                spaceForObject,
                spaceForObject,
                spaceForObject,
                spaceForObject,
                TableLayout.FILL
                }};


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new TableLayout(size));
        this.add(mainPanel);

        // text font setting by slider from 10 to 50
        JLabel textFontSizeLabel = new JLabel("Размер текста");
        textFontSizeLabel.setVerticalAlignment(SwingConstants.CENTER);
        textFontSizeLabel.setHorizontalAlignment(SwingConstants.LEFT);
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
        mainPanel.add(textFontSizeLabel, "0, 0");
        mainPanel.add(textFontSizeSlider, "1, 0");

        // buttons font setting by slider from 10 to 50
        JLabel buttonsFontSizeLabel = new JLabel("Размер текста кнопок");
        buttonsFontSizeLabel.setVerticalAlignment(SwingConstants.CENTER);
        buttonsFontSizeLabel.setHorizontalAlignment(SwingConstants.LEFT);
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
        mainPanel.add(buttonsFontSizeLabel, "0, 1");
        mainPanel.add(buttonsFontSizeSlider, "1, 1");

        // background color setting by radio buttons in button group
        JLabel backgroundColorLabel = new JLabel("Цвет фона");
        backgroundColorLabel.setVerticalAlignment(SwingConstants.CENTER);
        backgroundColorLabel.setHorizontalAlignment(SwingConstants.LEFT);
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
        mainPanel.add(backgroundColorLabel, "0, 2");
        mainPanel.add(backgroundColorButtonsPanel, "1, 2");

        // show help check
        JLabel helpChecksLabel = new JLabel("Справка");
        helpChecksLabel.setVerticalAlignment(SwingConstants.CENTER);
        helpChecksLabel.setHorizontalAlignment(SwingConstants.LEFT);
        helpChecksLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        JCheckBox showHelpCheck = new JCheckBox("Показывать на страничке, если имеется");
        showHelpCheck.setSelected(viewConfig.getShowHelp());
        showHelpCheck.addActionListener(e -> {
            viewConfig.setShowHelp(showHelpCheck.isSelected());
            parent.refresh();
        });
        JCheckBox separateHelp = new JCheckBox("Разделять подсказки по \"" + HelpManager.getSeparator() + "\"");
        separateHelp.setSelected(viewConfig.getShowHelp());
        separateHelp.addActionListener(e -> {
            viewConfig.setSeparateHelp(separateHelp.isSelected());
            parent.refresh();
        });
        JPanel helpChecksPanel = new JPanel();
        helpChecksPanel.setLayout(new TableLayout(new double[][]{{TableLayout.FILL},{0.25, 0.25, 0.25, 0.25}}));
        helpChecksPanel.setBackground(Color.WHITE);
        helpChecksPanel.add(showHelpCheck, "0, 1");
        helpChecksPanel.add(separateHelp, "0, 2");
        mainPanel.add(helpChecksLabel, "0, 3");
        mainPanel.add(helpChecksPanel, "1, 3");

        // show search check
        JLabel showSearchLabel = new JLabel("Поиск");
        showSearchLabel.setVerticalAlignment(SwingConstants.CENTER);
        showSearchLabel.setHorizontalAlignment(SwingConstants.LEFT);
        showSearchLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        JCheckBox showSearchCheck = new JCheckBox("Отображать сверху");
        showSearchCheck.setSelected(viewConfig.getShowSearch());
        showSearchCheck.addActionListener(e -> {
            viewConfig.setShowSearch(showSearchCheck.isSelected());
            parent.refresh();
        });
        JPanel showSearchCheckPanel = new JPanel();
        showSearchCheckPanel.setLayout(new GridLayout(1, 1));
        showSearchCheckPanel.setBackground(Color.WHITE);
        showSearchCheckPanel.add(showSearchCheck);
        mainPanel.add(showSearchLabel, "0, 4");
        mainPanel.add(showSearchCheckPanel, "1, 4");

        // save pos check
        JLabel savePosLabel = new JLabel("Позиция окна");
        savePosLabel.setVerticalAlignment(SwingConstants.CENTER);
        savePosLabel.setHorizontalAlignment(SwingConstants.LEFT);
        savePosLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        JCheckBox savePosCheckBox = new JCheckBox("Сохранять при закрыти программы");
        savePosCheckBox.setSelected(viewConfig.getSavePos());
        savePosCheckBox.addActionListener(e -> {
            viewConfig.setSavePos(savePosCheckBox.isSelected());
            parent.refresh();
        });
        JPanel savePosCheckPanel = new JPanel();
        savePosCheckPanel.setLayout(new GridLayout(1, 1));
        savePosCheckPanel.setBackground(Color.WHITE);
        savePosCheckPanel.add(savePosCheckBox);
        mainPanel.add(savePosLabel, "0, 5");
        mainPanel.add(savePosCheckPanel, "1, 5");

        // button to save settings
        JButton saveSettingsButton = new JButton("Сохранить");
        saveSettingsButton.addActionListener(e -> {
            viewConfig.saveConfig();
            parent.refresh();
            this.dispose();
        });
        mainPanel.add(saveSettingsButton, "0, 6, 1, 6");

        // for all components set background color to white
        for (Component component : mainPanel.getComponents()) {
            component.setBackground(Color.WHITE);
        }
        mainPanel.setBackground(Color.WHITE);

        this.setVisible(true);
    }
}
