package com.example.siderswebapp.exception.domain;

import static com.example.siderswebapp.exception.domain.ErrorUtils.*;

public class PostNotFoundException extends SidersException {

    public PostNotFoundException() {
        super(POST_NOT_FOUND.getMessage());

    }

    public PostNotFoundException(Throwable cause) {
        super(POST_NOT_FOUND.getMessage(), cause);
    }

    @Override
    public int getStatus() {
        return POST_NOT_FOUND.getStatus();
    }

    @Override
    public String getErrorCode() {
        return POST_NOT_FOUND.getErrorCode();
    }
}
