package com.androsov;

import com.androsov.gui.ViewConfig;
import com.androsov.gui.frames.StartFrame;

import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("AdminScriptLogger");
        LoggerConfigurer.configure(logger);

        ViewConfig viewConfig = ViewConfig.getInstance();
        viewConfig.readConfig();

        new StartFrame();
    }
}
