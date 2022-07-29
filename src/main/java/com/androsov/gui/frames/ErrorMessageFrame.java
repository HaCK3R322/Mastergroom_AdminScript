package com.androsov.gui.frames;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ErrorMessageFrame {
    private final static int ERROR_SUCCESS = 0x0;

    // this for error message
    private final JPanel panel = new JPanel();
    String errorMessage;

    public ErrorMessageFrame(String errorMessage) {
        this.errorMessage = errorMessage;

        JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    public ErrorMessageFrame(String errorMessage, boolean several) {

        if (several) {
            final JOptionPane optionPane = new JOptionPane(errorMessage, JOptionPane.ERROR_MESSAGE);
            final JDialog dialog = new JDialog(new JFrame(),
                    "Critical error",
                    true);
            // center dialog on screen
            dialog.setLocationRelativeTo(null);
            dialog.setContentPane(optionPane);
            dialog.setDefaultCloseOperation(
                    JDialog.DO_NOTHING_ON_CLOSE);
            dialog.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    System.exit(ERROR_SUCCESS);
                }
            });
            optionPane.addPropertyChangeListener(
                    e -> {
                        String prop = e.getPropertyName();
                        if (dialog.isVisible() &&
                                (e.getSource() == optionPane) &&
                                prop.equals(JOptionPane.VALUE_PROPERTY)) {
                            //If you were going to check something
                            //before closing the window, you'd do
                            //it here.
                            dialog.setVisible(false);
                            System.exit(ERROR_SUCCESS);
                        }
                    });

            dialog.pack();
            dialog.setVisible(true);
        } else {
            new ErrorMessageFrame(errorMessage);
        }
    }
}
