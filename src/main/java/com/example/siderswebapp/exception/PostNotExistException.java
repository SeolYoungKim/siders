package com.example.siderswebapp.exception;

public class PostNotExistException extends SidersException {

    private final static String ERROR_MESSAGE = "존재하지 않는 글입니다.";

    public PostNotExistException() {
        super(ERROR_MESSAGE);

    }

    public PostNotExistException(Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }

    @Override
    public int getStatus() {
        return 404;
    }

    @Override
    public String getErrorCode() {
        return "POST-ERR-404";
    }
}
