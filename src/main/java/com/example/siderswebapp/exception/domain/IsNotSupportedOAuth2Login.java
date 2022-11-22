package com.example.siderswebapp.exception.domain;

import static com.example.siderswebapp.exception.domain.ErrorUtils.*;

public class IsNotSupportedOAuth2Login extends SidersException {

    public IsNotSupportedOAuth2Login() {
        super(IS_NOT_SUPPORTED_OAUTH2_LOGIN.getMessage());
    }

    @Override
    public int getStatus() {
        return IS_NOT_SUPPORTED_OAUTH2_LOGIN.getStatus();
    }

    @Override
    public String getErrorCode() {
        return IS_NOT_SUPPORTED_OAUTH2_LOGIN.getErrorCode();
    }
}
