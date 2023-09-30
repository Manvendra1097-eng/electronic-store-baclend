package com.m2code.exception;

public class BadApiRequestException extends RuntimeException {

    public BadApiRequestException() {
        super("Bad request");
    }

    public BadApiRequestException(String message) {
        super(message);
    }
}
