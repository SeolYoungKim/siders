package com.example.siderswebapp.auth.oauth.service;

import lombok.Getter;

@Getter
public enum ProviderInfo {

    GOOGLE(
            "google",
            "sub",
            "name",
            "email",
            "picture"
    ),
    GITHUB(
            "github",
            "id",
            "name",
            "login",
            "avatar_url"

    ),
    NAVER(
            "naver",
            "id",
            "name",
            "email",
            "profile_image"
    ),
    KAKAO(
            "kakao",
            "id",
            "nickname",
            "email",
            "profile_image_url"
    );

    private final String registrationId;
    private final String authId;
    private final String email;
    private final String name;
    private final String picture;

    ProviderInfo(String registrationId, String authId,
                 String email, String name, String picture) {
        this.registrationId = registrationId;
        this.authId = authId;
        this.email = email;
        this.name = name;
        this.picture = picture;
    }
}
