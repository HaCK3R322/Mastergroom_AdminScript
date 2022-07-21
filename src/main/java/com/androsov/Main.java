package com.androsov;

import com.androsov.gui.ErrorMessageFrame;
import com.androsov.gui.Gui;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("AdminScriptLogger");
        LoggerConfigurer.configure(logger);
        new Gui();
    }
}
