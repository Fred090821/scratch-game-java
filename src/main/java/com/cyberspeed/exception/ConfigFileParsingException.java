package com.cyberspeed.exception;

public class ConfigFileParsingException extends RuntimeException {

    public ConfigFileParsingException(String message) {
        super(message);
    }

    public ConfigFileParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}