package com.androsov;

import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.ScriptFrame;

import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("AdminScriptLogger");
        LoggerConfigurer.configureLogger(logger);
        logger.info("Logger configured. Logger name: " + logger.getName());

        ViewConfig viewConfig = ViewConfig.getInstance();
        viewConfig.readConfig();

        // start script frame
        logger.info("Opening script frame");
        ScriptFrame scriptFrame = new ScriptFrame();

        // close logger when script frame is closed
        scriptFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                logger.info("Script frame closed. End of program.");
                LoggerConfigurer.closeLogger(logger);
            }
        });
    }
}
