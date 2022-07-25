package com.androsov.gui;

import com.androsov.ConfigSerializer;
import com.androsov.gui.frames.ErrorMessageFrame;
import lombok.Getter;
import lombok.Setter;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ViewConfig {
    //singleton
    private static ViewConfig instance;
    public static ViewConfig getInstance() {
        if (instance == null) {
            instance = new ViewConfig();
        }
        return instance;
    }
    private ViewConfig() {}

    @Getter @Setter private Integer textFontSize = 20;
    @Getter @Setter private Integer buttonsFontSize = 20;
    @Getter @Setter private Color backgroundColor = Color.WHITE;
    @Getter @Setter private Integer frameSizeX = 800;
    @Getter @Setter private Integer frameSizeY = 800;
    @Getter @Setter private Integer settingsFrameSizeX = 500;
    @Getter @Setter private Integer settingsFrameSizeY = 500;

    public void readConfig() {
        try {
            Map<String, String> propertiesMap = ConfigSerializer.readConfig("resources/view.config");
            ViewConfig.getInstance().setTextFontSize(Integer.parseInt(propertiesMap.get("textFontSize")));
            ViewConfig.getInstance().setButtonsFontSize(Integer.parseInt(propertiesMap.get("buttonsFontSize")));
            ViewConfig.getInstance().setBackgroundColor(Color.decode(propertiesMap.get("backgroundColor")));
            ViewConfig.getInstance().setFrameSizeX(Integer.parseInt(propertiesMap.get("frameSizeX")));
            ViewConfig.getInstance().setFrameSizeY(Integer.parseInt(propertiesMap.get("frameSizeY")));
            ViewConfig.getInstance().setSettingsFrameSizeX(Integer.parseInt(propertiesMap.get("settingsFrameSizeX")));
            ViewConfig.getInstance().setSettingsFrameSizeY(Integer.parseInt(propertiesMap.get("settingsFrameSizeY")));
        } catch (Exception e) {
            new ErrorMessageFrame("Error reading config file: " + e.getMessage() + "\nDefault file will be used");
        }
    }

    public void saveConfig() {
        try {
            ConfigSerializer.saveConfig("resources/view.config", getPropertiesStringMap());
        } catch (Exception e) {
            new ErrorMessageFrame("Error saving config file: " + e.getMessage());
        }
    }

    public Map<String, String> getPropertiesStringMap() {
        Map<String, String> propertiesMap = new HashMap<>();
        propertiesMap.put("textFontSize", String.valueOf(ViewConfig.getInstance().getTextFontSize()));
        propertiesMap.put("buttonsFontSize", String.valueOf(ViewConfig.getInstance().getButtonsFontSize()));
        propertiesMap.put("backgroundColor", String.valueOf(ViewConfig.getInstance().getBackgroundColor().getRGB()));
        propertiesMap.put("frameSizeX", String.valueOf(ViewConfig.getInstance().getFrameSizeX()));
        propertiesMap.put("frameSizeY", String.valueOf(ViewConfig.getInstance().getFrameSizeY()));
        propertiesMap.put("settingsFrameSizeX", String.valueOf(ViewConfig.getInstance().getSettingsFrameSizeX()));
        propertiesMap.put("settingsFrameSizeY", String.valueOf(ViewConfig.getInstance().getSettingsFrameSizeY()));
        return propertiesMap;
    }
}
