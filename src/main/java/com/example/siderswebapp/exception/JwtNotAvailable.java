package com.example.siderswebapp.exception;

import static com.example.siderswebapp.exception.ErrorUtils.*;

public class JwtNotAvailable extends SidersException {

    public JwtNotAvailable(String message) {
        super(message);
    }

    @Override
    public int getStatus() {
        return JWT_NOT_AVAILABLE.getStatus();
    }

    @Override
    public String getErrorCode() {
        return JWT_NOT_AVAILABLE.getErrorCode();
    }
}
