package com.androsov.gui.frames.help;

import com.androsov.ConfigSerializer;
import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.DefaultFrame;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HelpFrame extends DefaultFrame {
    private static ViewConfig viewConfig = ViewConfig.getInstance();

    public HelpFrame(String helpTopic, String helpText) {
        super(viewConfig.getSettingsFrameSizeX() / 2, viewConfig.getSettingsFrameSizeY(), true);

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

    private static String stringToHtmlText(String text) {
        return "<html><p>" + text + "</p></html>";
    }
}
