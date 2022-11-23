package com.example.siderswebapp.auth.oauth.handler;

import com.example.siderswebapp.auth.UriList;
import org.springframework.web.util.UriComponentsBuilder;

enum RedirectUri {
    LOGIN_FAIL(false),
    LOGIN_SUCCESS(true),
    ;

    private static final String PARAMETER_NAME = "loginSuccess";

    private final String redirectUri;

    RedirectUri(boolean isLoginSuccess) {
        this.redirectUri = UriComponentsBuilder.fromUriString(UriList.FRONT_END.getUri())
                .queryParam(PARAMETER_NAME, isLoginSuccess)
                .build()
                .toUriString();
    }

    String redirectUri() {
        return redirectUri;
    }
}
