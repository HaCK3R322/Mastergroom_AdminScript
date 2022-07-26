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

        JTextArea textArea = new JTextArea(helpText);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        textArea.setBackground(viewConfig.getBackgroundColor());
        this.add(textArea);

        this.setVisible(true);
    }
}
