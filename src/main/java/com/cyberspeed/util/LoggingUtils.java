package com.cyberspeed.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The {@code LoggingUtils} class provides utility methods for configuring and accessing
 * the global logger used throughout the application. It sets up a console handler with
 * a simple text formatter and ensures that log messages are output at the INFO level.
 * The class offers a static method to retrieve a logger instance by name, allowing
 * consistent logging practices across different components of the application.
 */
public final class LoggingUtils {

    private LoggingUtils() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    // Configure the logger for the console output with INFO level
    static {
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);  // Set log level to INFO
        consoleHandler.setFormatter(new SimpleFormatter());  // Use simple text format
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);  // Use the global logger
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.INFO);  // Ensure the logger is set to INFO level
    }

    // Provide access to the global logger
    public static Logger getLogger(String name) {
        return Logger.getLogger(name);
    }
}
