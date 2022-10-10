package com.example.siderswebapp.web.controller;

import com.example.siderswebapp.exception.SidersException;
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
    @ExceptionHandler(SidersException.class)
    public ErrorResult commonExceptionHandler(SidersException e) {
        return ErrorResult.builder()
                .status(e.getStatus())
                .code(e.getErrorCode())
                .message(e.getMessage())
                .build();
    }

    //TODO: JWT 인증 객체가 아닌, OAUTH2 인증 객체가 접근하려 할 때 예외 처리
    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(IllegalStateException.class)
    public ErrorResult illegalArgumentExceptionHandler(IllegalStateException e) {
        return ErrorResult.builder()
                .status(400)
                .code("ILLEGAL_STATE-ERR-400")
                .message(e.getMessage())
                .build();
    }

    @ResponseStatus(code = BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalArgumentExceptionHandler(IllegalArgumentException e) {
        return ErrorResult.builder()
                .status(400)
                .code("ILLEGAL_ARGS-ERR-400")
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
