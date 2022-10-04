package com.example.siderswebapp.exception;

public class JwtNotAvailable extends SidersException {

    public JwtNotAvailable(String message) {
        super(message);
    }

    @Override
    public int getStatus() {
        return 403;
    }

    @Override
    public String getErrorCode() {
        return "JWT-ERR-403";
    }
}
