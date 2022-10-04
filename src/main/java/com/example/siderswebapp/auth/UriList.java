package com.example.siderswebapp.auth;

import lombok.Getter;

@Getter
public enum UriList {

    FRONT_END("http://localhost:8082");

    private final String uri;

    UriList(String uri) {
        this.uri = uri;
    }
}
