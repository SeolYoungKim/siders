package com.example.siderswebapp.exception.presentation.dto;

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
