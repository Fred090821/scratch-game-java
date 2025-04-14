package com.cyberspeed.exception;

public class InvalidGameConfigurationException extends RuntimeException {
    public InvalidGameConfigurationException(String message) {
        super(message);
    }
}