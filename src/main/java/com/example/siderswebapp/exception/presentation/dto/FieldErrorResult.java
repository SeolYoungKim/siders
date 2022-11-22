package com.example.siderswebapp.exception.presentation.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class FieldErrorResult {

    private final int status;
    private final String code;
    private final List<FieldsErrorInfo> errors = new ArrayList<>();

    @Builder
    public FieldErrorResult(int status, String code) {
        this.status = status;
        this.code = code;
    }

    public void addErrors(FieldsErrorInfo fieldsErrorInfo) {
        errors.add(fieldsErrorInfo);
    }

}
