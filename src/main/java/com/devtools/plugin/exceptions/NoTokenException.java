package com.devtools.plugin.exceptions;

public class NoTokenException extends Exception {
    public NoTokenException(String message) {
        super(message);
    }

    public NoTokenException(Throwable cause) {
        super(cause);
    }

    public NoTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
