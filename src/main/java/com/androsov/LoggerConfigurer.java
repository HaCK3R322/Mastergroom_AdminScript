package com.androsov;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class LoggerConfigurer {
    public static Logger getConfiguredLogger(String loggerName) {
        Logger logger = Logger.getLogger(loggerName);
        configure(logger);
        return logger;
    }

    public static void configure(Logger logger) {
        try {
            FileHandler fh = new FileHandler("logs/AdminScript " + getCurrentDate() + ".log");
            FileHandler latestFh = new FileHandler("logs/latest.log");
            ConsoleHandler ch = new ConsoleHandler();

            SimpleFormatter formatter = new SimpleFormatter() {
                private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

                @Override
                public synchronized String format(LogRecord lr) {
                    return String.format(format,
                            new Date(lr.getMillis()),
                            lr.getLevel().getLocalizedName(),
                            lr.getMessage()
                    );
                }
            };

            fh.setFormatter(formatter);
            latestFh.setFormatter(formatter);
            ch.setFormatter(formatter);

            logger.setUseParentHandlers(false);
            logger.addHandler(fh);
            logger.addHandler(latestFh);
            logger.addHandler(ch);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Failed to configure logger");
            e.printStackTrace();
        }
    }

    private static String getCurrentDate() {
        final String DATE_FORMAT_NOW = "yyyy-MM-dd-HH-mm-ss";
        String date = new SimpleDateFormat(DATE_FORMAT_NOW).format(new Date());
        return date.toString();
    }
}
