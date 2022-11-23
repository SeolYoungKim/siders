package com.example.siderswebapp.auth.oauth.handler;

import com.example.siderswebapp.auth.UriList;
import org.springframework.web.util.UriComponentsBuilder;

enum RedirectUri {
    LOGIN_FAIL(false),
    LOGIN_SUCCESS(true),
    ;

    private static final String PARAMETER_NAME = "loginSuccess";

    private static final UriComponentsBuilder URI_BUILDER = UriComponentsBuilder
            .fromUriString(UriList.FRONT_END.getUri());

    private final boolean isLoginSuccess;

    RedirectUri(boolean isLoginSuccess) {
        this.isLoginSuccess = isLoginSuccess;
    }

    String redirectUri() {
        return URI_BUILDER
                .queryParam(PARAMETER_NAME, isLoginSuccess)
                .build()
                .toUriString();
    }
}
