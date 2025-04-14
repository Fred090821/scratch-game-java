package com.cyberspeed.exception;

public class InvalidBettingAmountException extends RuntimeException {

    public InvalidBettingAmountException(String message) {
        super(message);
    }

    public InvalidBettingAmountException(String message, Throwable cause) {
        super(message, cause);
    }
}
