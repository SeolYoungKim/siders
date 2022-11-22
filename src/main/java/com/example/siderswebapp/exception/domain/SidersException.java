package com.example.siderswebapp.exception.domain;

import lombok.Getter;

@Getter
public abstract class SidersException extends RuntimeException {

    public SidersException(String message) {
        super(message);
    }

    public SidersException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatus();
    public abstract String getErrorCode();
}
