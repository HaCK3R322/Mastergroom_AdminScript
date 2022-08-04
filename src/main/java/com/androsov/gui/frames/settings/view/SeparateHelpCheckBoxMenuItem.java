package com.androsov.gui.frames.settings.view;

import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.DefaultFrame;

import javax.swing.*;

public class SeparateHelpCheckBoxMenuItem extends JCheckBoxMenuItem {
    private final static ViewConfig viewConfig = ViewConfig.getInstance();

    public SeparateHelpCheckBoxMenuItem(DefaultFrame parent, String text) {
        super(text);
        this.setSelected(viewConfig.getSeparateHelp());
        this.addActionListener(e -> {
            viewConfig.setSeparateHelp(this.isSelected());
            viewConfig.saveConfig();
            parent.refresh();
        });
    }
}
