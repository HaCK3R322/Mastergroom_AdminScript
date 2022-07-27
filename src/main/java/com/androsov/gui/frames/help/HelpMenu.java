package com.androsov.gui.frames.help;

import javax.swing.*;
import java.util.Map;

public class HelpMenu extends JMenu {
    public HelpMenu(String name) {
        super(name);

        Map<Integer, HelpManager.HelpNode> helpMap = HelpManager.getHelpMap();

        if(!helpMap.isEmpty()) {
            for (Map.Entry<Integer, HelpManager.HelpNode> entry : helpMap.entrySet()) {
                if(entry.getValue().getNodeId() == null) {
                    JMenuItem helpMenuItem = new JMenuItem(entry.getValue().getHelpTopic());
                    helpMenuItem.addActionListener(e -> {
                        new HelpFrame(entry.getValue().getHelpTopic(), entry.getValue().getHelpText());
                    });
                    this.add(helpMenuItem);
                }
            }
        }
    }
}

