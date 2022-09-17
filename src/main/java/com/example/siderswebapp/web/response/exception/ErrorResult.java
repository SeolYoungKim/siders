package com.example.siderswebapp.web.response.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResult {

    private final int status;
    private final String message;
    private final String code;

    @Builder
    public ErrorResult(int status, String message, String code) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
