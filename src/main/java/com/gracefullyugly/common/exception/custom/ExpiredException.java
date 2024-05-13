package com.gracefullyugly.common.exception.custom;

public class ExpiredException extends RuntimeException{

    public ExpiredException(String message) {
        super(message);
    }

    public ExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
