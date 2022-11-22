package com.example.siderswebapp.exception.domain;

import static com.example.siderswebapp.exception.domain.ErrorUtils.*;

public class MemberNotFoundException extends SidersException {

    public MemberNotFoundException() {
        super(MEMBER_NOT_FOUND.getMessage());
    }

    public MemberNotFoundException(Throwable cause) {
        super(MEMBER_NOT_FOUND.getMessage(), cause);
    }

    @Override
    public int getStatus() {
        return MEMBER_NOT_FOUND.getStatus();
    }

    @Override
    public String getErrorCode() {
        return MEMBER_NOT_FOUND.getErrorCode();
    }
}
