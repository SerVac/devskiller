package com.exchange.model;

public class RateUnavailableException extends Exception {
    public RateUnavailableException() {
    }

    public RateUnavailableException(String message) {
        super(message);
    }
}
