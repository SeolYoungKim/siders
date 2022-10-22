package com.example.siderswebapp.auth.oauth.service;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonAttributes {
    private final String authId;
    private final String email;
    private final String name;
    private final String picture;

    @Builder
    public CommonAttributes(String authId, String email, String name, String picture) {
        this.authId = authId;
        this.email = email;
        this.name = name;
        this.picture = picture;
    }
}
