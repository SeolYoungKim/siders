package com.example.siderswebapp.exception.domain;

import static com.example.siderswebapp.exception.domain.ErrorUtils.*;

public class IsNotOwnerException extends SidersException {

    public IsNotOwnerException() {
        super(IS_NOT_OWNER.getMessage());
    }

    public IsNotOwnerException(Throwable cause) {
        super(IS_NOT_OWNER.getMessage(), cause);
    }

    @Override
    public int getStatus() {
        return IS_NOT_OWNER.getStatus();
    }

    @Override
    public String getErrorCode() {
        return IS_NOT_OWNER.getErrorCode();
    }
}
