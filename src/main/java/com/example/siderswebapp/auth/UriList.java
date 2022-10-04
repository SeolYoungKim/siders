package com.example.siderswebapp.auth;

import lombok.Getter;

@Getter
public enum UriList {

    FRONT_END("http://localhost:3000");

    private final String uri;

    UriList(String uri) {
        this.uri = uri;
    }
}
