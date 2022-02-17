package com.exchange.model;

public class QuoteOutdatedException extends Exception {
    public QuoteOutdatedException() {
    }

    public QuoteOutdatedException(String message) {
        super(message);
    }
}
