package com.stepupbackend.exception;

public class UnauthorizedActionException extends RuntimeException {

    public UnauthorizedActionException(String action) {
        super("Not authorized to " + action);
    }
}
