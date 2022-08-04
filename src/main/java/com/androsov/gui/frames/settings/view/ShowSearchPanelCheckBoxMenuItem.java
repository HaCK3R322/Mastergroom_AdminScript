package com.androsov.gui.frames.settings.view;

import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.DefaultFrame;

import javax.swing.JCheckBoxMenuItem;


public class ShowSearchPanelCheckBoxMenuItem extends JCheckBoxMenuItem {
     private static final ViewConfig viewConfig = ViewConfig.getInstance();

     public ShowSearchPanelCheckBoxMenuItem(DefaultFrame parent, String text) {
         super(text);
         this.setSelected(viewConfig.getShowSearch());
         this.addActionListener(e -> {
             viewConfig.setShowSearch(this.isSelected());
             parent.refresh();
         });
     }
}
