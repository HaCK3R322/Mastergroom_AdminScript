package com.androsov.gui.frames.settings;

import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.DefaultFrame;
import com.androsov.gui.frames.EscClosableDefaultFrame;
import com.androsov.node.Node;
import com.androsov.node.NodeManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class PhraseRedactorFrame extends EscClosableDefaultFrame {
    private static final ViewConfig viewConfig = ViewConfig.getInstance();
    private final NodeManager nodeManager;
    
    public PhraseRedactorFrame(DefaultFrame parentFrame, Node nodeToRedact) {
        super(viewConfig.getSettingsFrameSizeX(), viewConfig.getSettingsFrameSizeY(), false);
        
        nodeManager = NodeManager.getInstance();

        JFrame phraseRedactorFrame = new JFrame();
        phraseRedactorFrame.setSize(viewConfig.getSettingsFrameSizeX(), viewConfig.getSettingsFrameSizeY() / 2);
        phraseRedactorFrame.setResizable(false);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        phraseRedactorFrame.setLocation(dim.width / 2 - phraseRedactorFrame.getSize().width / 2, dim.height / 2 - phraseRedactorFrame.getSize().height / 2);

        JPanel phraseRedactorPanel = new JPanel();
        phraseRedactorPanel.setOpaque(true);
        phraseRedactorFrame.setBackground(viewConfig.getBackgroundColor());
        phraseRedactorPanel.setLayout(new GridLayout(2, 1));
        phraseRedactorFrame.add(phraseRedactorPanel);

        // text filed for phrase
        JTextField phraseTextField = new JTextField();
        phraseTextField.setText(nodeToRedact.getPhrase());
        phraseRedactorPanel.add(phraseTextField);

        // button to save phrase
        JPanel savePhrasePanel = new JPanel();
        savePhrasePanel.setLayout(new GridLayout(1, 1));
        savePhrasePanel.setOpaque(false);
        JButton savePhraseButton = new JButton("Сохранить");
        savePhraseButton.addActionListener(e -> {
            nodeToRedact.setPhrase(phraseTextField.getText());
            try {
                nodeManager.saveNodes();
            } catch (IOException ex) {
                Logger.getLogger("AdminScriptLogger").log(Level.WARNING, null, ex);

            }
            phraseRedactorFrame.dispose();
            parentFrame.refresh();
        });
        savePhrasePanel.add(savePhraseButton);
        phraseRedactorPanel.add(savePhrasePanel);
        phraseRedactorFrame.setVisible(true);
    }
}
