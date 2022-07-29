package com.androsov.gui.frames.help;

import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.DefaultFrame;
import com.androsov.gui.frames.ErrorMessageFrame;
import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HelpRedactorFrame extends DefaultFrame {
    private static final ViewConfig viewConfig = ViewConfig.getInstance();

    public HelpRedactorFrame(DefaultFrame parent, Integer helpNodeId) {
        super(viewConfig.getHelpFrameSizeX(), viewConfig.getHelpFrameSizeY(), true);

        try {
            this.setResizable(true);

            this.setTitle("Редактирование подсказки для страницы id:" + helpNodeId);

            String topic = "";
            String text = "";

            if (HelpManager.isHelpExists(helpNodeId)) {
                HelpManager.HelpNode helpNode = HelpManager.getHelpMap().get(helpNodeId);
                topic = helpNode.getHelpTopic();
                text = helpNode.getHelpText();
            }

            double[][] size = {{0.33, TableLayout.FILL}, {0.1, 0.775, 0.125}};
            JPanel mainPanel = new JPanel(new TableLayout(size));
            mainPanel.setOpaque(true);
            mainPanel.setBackground(viewConfig.getBackgroundColor());

            JLabel topicLabel = new JLabel("Тема: ");
            topicLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            topicLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
            mainPanel.add(topicLabel, "0,0");

            JTextField topicTextField = new JTextField(topic);
            mainPanel.add(topicTextField, "1,0");

            JLabel textLabel = new JLabel("Текст: ");
            textLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            textLabel.setVerticalAlignment(SwingConstants.TOP);
            mainPanel.add(textLabel, "0,1");

            JTextArea textTextArea = new JTextArea(text);
            textTextArea.setEditable(true);
            textTextArea.setLineWrap(true);
            JScrollPane textScrollPane = new JScrollPane(textTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            mainPanel.add(textScrollPane, "1,1");

            double[][] buttonsTableSize = {{0.5, 0.5}, {TableLayout.FILL}};
            JPanel buttonsPanel = new JPanel(new TableLayout(buttonsTableSize));
            mainPanel.add(buttonsPanel, "0,2, 1,2");

            JButton saveButton = new JButton("Сохранить");
            saveButton.addActionListener(e -> {
                HelpManager.getHelpMap().put(helpNodeId, new HelpManager.HelpNode(helpNodeId, topicTextField.getText(), textTextArea.getText()));
                try {
                    HelpManager.save();
                } catch (IOException ex) {
                    Logger.getLogger("AdminScriptLogger").log(Level.WARNING, "Cannot save help: " + ex.getMessage());
                    new ErrorMessageFrame("Не удалось сохранить подсказки: " + ex.getMessage());
                }
                parent.refresh();
                this.dispose();
            });
            JPanel savePanel = new JPanel(new GridLayout(1, 1));
            savePanel.add(saveButton);
            buttonsPanel.add(savePanel, "0,0");

            // delete button
            JButton deleteButton = new JButton("Удалить");
            deleteButton.addActionListener(e -> {
                // show confirmation dialog
                int result = JOptionPane.showConfirmDialog(this, "Вы действительно хотите удалить подсказку?", "Подтверждение", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    HelpManager.getHelpMap().remove(helpNodeId);
                    try {
                        HelpManager.save();
                    } catch (IOException ex) {
                        Logger.getLogger("AdminScriptLogger").log(Level.WARNING, "Cannot save help: " + ex.getMessage());
                        new ErrorMessageFrame("Не удалось сохранить подсказки: " + ex.getMessage());
                    }
                    parent.refresh();
                    this.dispose();
                }
            });
            JPanel deletePanel = new JPanel(new GridLayout(1, 1));
            deletePanel.add(deleteButton);
            buttonsPanel.add(deletePanel, "1,0");

            this.add(mainPanel);
            this.setVisible(true);
        } catch (Exception e) {
            Logger.getLogger("AdminScriptLogger").log(Level.WARNING, "Cannot create help redactor frame: " + e.getMessage());
            new ErrorMessageFrame("Не удалось создать окно редактирования подсказки: " + e.getMessage());
        }
    }
}
