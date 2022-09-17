package com.example.siderswebapp.web.controller;

import com.example.siderswebapp.exception.CommonException;
import com.example.siderswebapp.web.response.exception.ErrorResult;
import com.example.siderswebapp.web.response.exception.FieldErrorResult;
import com.example.siderswebapp.web.response.exception.FieldsErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(code = NOT_FOUND)
    @ExceptionHandler(CommonException.class)
    public ErrorResult commonExceptionHandler(CommonException e) {
        return ErrorResult.builder()
                .status(e.getStatus())
                .code(e.getErrorCode())
                .message(e.getMessage())
                .build();
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public FieldErrorResult fieldErrorHandler(MethodArgumentNotValidException e) {

        FieldErrorResult fieldErrorResult = FieldErrorResult.builder()
                .status(400)
                .code("FIELDS-ERR-400")
                .build();

        e.getFieldErrors()
                .forEach(fieldErrors
                        -> fieldErrorResult.addErrors(FieldsErrorInfo.builder()
                        .fieldsName(fieldErrors.getField())
                        .message(fieldErrors.getDefaultMessage())
                        .build()));

        return fieldErrorResult;
    }
}
