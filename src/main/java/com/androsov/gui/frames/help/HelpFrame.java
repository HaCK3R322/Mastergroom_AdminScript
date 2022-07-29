package com.androsov.gui.frames.help;

import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.DefaultFrame;

import javax.swing.*;

public class HelpFrame extends DefaultFrame {
    private static final ViewConfig viewConfig = ViewConfig.getInstance();

    public HelpFrame(String helpTopic, String helpText) {
        super(viewConfig.getHelpFrameSizeX(), viewConfig.getHelpFrameSizeY(), true);

        this.setTitle(helpTopic);

        JTextPane textPanel = new JTextPane();
        textPanel.setContentType("text/html");
        textPanel.setText(helpText);
        textPanel.setEditable(false);
        textPanel.setOpaque(true);
        textPanel.setBackground(viewConfig.getBackgroundColor());

        this.add(textPanel);

        this.setVisible(true);
    }
}
