package com.example.siderswebapp.exception;

import lombok.Getter;

@Getter
public abstract class CommonException extends RuntimeException {

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatus();
    public abstract String getErrorCode();
}
