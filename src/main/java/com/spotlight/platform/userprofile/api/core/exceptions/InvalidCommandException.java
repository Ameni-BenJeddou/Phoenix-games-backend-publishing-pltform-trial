package com.spotlight.platform.userprofile.api.core.exceptions;

public class InvalidCommandException extends RuntimeException {
    public InvalidCommandException(String errorMessage) {
        super(errorMessage);
    }
}

