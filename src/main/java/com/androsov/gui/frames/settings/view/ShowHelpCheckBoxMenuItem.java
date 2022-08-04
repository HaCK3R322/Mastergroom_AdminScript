package com.androsov.gui.frames.settings.view;

import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.DefaultFrame;

import javax.swing.*;

public class ShowHelpCheckBoxMenuItem extends JCheckBoxMenuItem {
    private static final ViewConfig viewConfig = ViewConfig.getInstance();

    public ShowHelpCheckBoxMenuItem(DefaultFrame parent, String text) {
        super(text);
        this.setSelected(viewConfig.getShowHelp());
        this.addActionListener(e -> {
            viewConfig.setShowHelp(this.isSelected());
            parent.refresh();
        });
    }
}
