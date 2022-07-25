package com.androsov.gui.frames;

import javax.swing.*;
import java.awt.*;

class MyFrame extends JFrame {
    public MyFrame() {
        final int width = 800;
        final int height = 800;
        final boolean resizable = true;

        this.setSize(width, height);
        this.setResizable(resizable);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - width / 2, dim.height / 2 - height / 2);
    }

    public MyFrame(Integer width, Integer height, boolean resizable) {
        this.setSize(width, height);
        this.setResizable(resizable);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - width / 2, dim.height / 2 - height / 2);
    }

    public void refresh() {
        this.revalidate();
        this.repaint();
    }
}
