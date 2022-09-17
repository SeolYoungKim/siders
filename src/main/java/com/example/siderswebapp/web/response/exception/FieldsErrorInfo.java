package com.example.siderswebapp.web.response.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FieldsErrorInfo {
    private final String fieldsName;
    private final String message;

    @Builder
    public FieldsErrorInfo(String fieldsName, String message) {
        this.fieldsName = fieldsName;
        this.message = message;
    }
}
