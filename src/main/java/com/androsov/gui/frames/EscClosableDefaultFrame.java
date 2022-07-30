package com.androsov.gui.frames;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class EscClosableDefaultFrame extends DefaultFrame {
    public EscClosableDefaultFrame() {
        super();
        addCloseOnEscapeListener(this);
    }

    public EscClosableDefaultFrame(Integer width, Integer height, boolean resizable) {
        super(width, height, resizable);
        addCloseOnEscapeListener(this);
    }

    public EscClosableDefaultFrame(Integer width, Integer height, boolean resizable, Integer posX, Integer posY) {
        super(width, height, resizable, posX, posY);
        addCloseOnEscapeListener(this);
    }

    private static void addCloseOnEscapeListener(EscClosableDefaultFrame frame) {
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "close");
        frame.getRootPane().getActionMap().put("close", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }
}
