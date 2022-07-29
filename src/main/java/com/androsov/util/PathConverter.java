package com.androsov.util;

public class PathConverter {
    public static String convertToAbsoluteAppdataFilePath(String localPath) {
        String appdataPath = System.getenv("APPDATA");
        appdataPath = appdataPath.replace("\\", "/");
        return appdataPath + "/Ivan Androsov/AdminScript/" + localPath;
    }
}
