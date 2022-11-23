package com.example.siderswebapp.auth.oauth.service;

import com.example.siderswebapp.exception.domain.IsNotSupportedOAuth2Login;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum OAuth2ProviderInfo {

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

    private static final Map<String, OAuth2ProviderInfo> REGISTRATION_ID_PROVIDER_INFO =
            Arrays.stream(values())
                    .collect(Collectors.toUnmodifiableMap(
                            OAuth2ProviderInfo -> OAuth2ProviderInfo.registrationId,
                            Function.identity()));

    private final String registrationId;
    private final String authIdKey;
    private final String emailKey;
    private final String nameKey;
    private final String pictureKey;

    OAuth2ProviderInfo(String registrationId, String authIdKey,
                 String emailKey, String nameKey, String pictureKey) {
        this.registrationId = registrationId;
        this.authIdKey = authIdKey;
        this.emailKey = emailKey;
        this.nameKey = nameKey;
        this.pictureKey = pictureKey;
    }

    static OAuth2ProviderInfo of(String registrationId) {
        if (!REGISTRATION_ID_PROVIDER_INFO.containsKey(registrationId)) {
            throw new IsNotSupportedOAuth2Login();
        }

        return REGISTRATION_ID_PROVIDER_INFO.get(registrationId);
    }

    boolean isGoogleOrGitHub() {
        return this == GOOGLE || this == GITHUB;
    }

    boolean isNaver() {
        return this == NAVER;
    }

    boolean isKakao() {
        return this == KAKAO;
    }

    Object findAuthId(Map<String, Object> attributes) {
        return String.valueOf(attributes.get(authIdKey));
    }

    Object findEmail(Map<String, Object> attributes) {
        return String.valueOf(attributes.get(emailKey));
    }

    Object findName(Map<String, Object> attributes) {
        return String.valueOf(attributes.get(nameKey));
    }

    Object findPicture(Map<String, Object> attributes) {
        return String.valueOf(attributes.get(pictureKey));
    }
}
