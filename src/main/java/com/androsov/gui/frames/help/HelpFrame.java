package com.androsov.gui.frames.help;

import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.DefaultFrame;
import com.androsov.gui.frames.EscClosableDefaultFrame;
import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;

public class HelpFrame extends EscClosableDefaultFrame {
    private static final ViewConfig viewConfig = ViewConfig.getInstance();

    public HelpFrame(String helpTopic, String helpText) {
        super(viewConfig.getHelpFrameSizeX(), viewConfig.getHelpFrameSizeY(), true);

        this.setTitle(helpTopic);

        JTextPane textPanel = new JTextPane();

        if(helpText.contains(HelpManager.getSeparator())) {
            String text2 = helpText.substring(helpText.indexOf(HelpManager.getSeparator()) + HelpManager.getSeparator().length());
            HelpFrame newHelpFrame = new HelpFrame(helpTopic, text2);
            newHelpFrame.setBounds(this.getX() + 50, this.getY() + 50, newHelpFrame.getWidth(), newHelpFrame.getHeight());

            helpText = helpText.substring(0, helpText.indexOf(HelpManager.getSeparator()));
        }
        textPanel.setText(helpText);

        textPanel.setFont(new Font("Monospaced", Font.PLAIN, 20));
        textPanel.setEditable(false);
        textPanel.setOpaque(true);
        textPanel.setBackground(viewConfig.getBackgroundColor());
        textPanel.setCaretPosition(0);

        JScrollPane scrollPane = new JScrollPane(textPanel);
        scrollPane.setPreferredSize(new Dimension(viewConfig.getHelpFrameSizeX(), viewConfig.getHelpFrameSizeY()));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setOpaque(true);
        scrollPane.setBackground(viewConfig.getBackgroundColor());



        this.add(scrollPane);
        this.setVisible(true);
    }
}
