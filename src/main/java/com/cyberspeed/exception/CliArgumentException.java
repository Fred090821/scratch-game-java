package com.cyberspeed.exception;

public class CliArgumentException extends RuntimeException {

    public CliArgumentException(String message) {
        super(message);
    }

    public CliArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}