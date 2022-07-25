package com.androsov;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class LoggerConfigurer {
    public static void configure(Logger logger) {
        try {
            SimpleFormatter formatter = new SimpleFormatter() {
                //private static final String format = "[%1$tF %1$tT] [%3$-47s] [%2$-7s] %4$s %n";
                private static final String format = "[%1$tF %1$tT] [%3$s/%2$s] %4$s %n";

                @Override
                public synchronized String format(LogRecord lr) {
                    return String.format(format,
                            new Date(lr.getMillis()),
                            lr.getLevel().getLocalizedName(),
                            lr.getSourceClassName() + "." + lr.getSourceMethodName(),
                            lr.getMessage()
                    );
                }
            };

            logger.setUseParentHandlers(false);

            FileHandler fileHandler = new FileHandler("logs/AdminScript " + getCurrentDate() + ".log", false);
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);

            FileHandler latestFileHandler = new FileHandler("logs/latest.log", false);
            latestFileHandler.setFormatter(formatter);
            logger.addHandler(latestFileHandler);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(formatter);
            logger.addHandler(consoleHandler);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to configure logger");
            e.printStackTrace();
        }
    }

    private static String getCurrentDate() {
        final String DATE_FORMAT_NOW = "yyyy-MM-dd-HH-mm-ss";
        return new SimpleDateFormat(DATE_FORMAT_NOW).format(new Date());
    }
}
