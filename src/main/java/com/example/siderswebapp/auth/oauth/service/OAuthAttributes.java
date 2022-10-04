package com.example.siderswebapp.auth.oauth.service;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 각각 다른 곳에서 로그인을 요청한 유저의 attributes 정보를 평준화 하는 클래스
 */

//TODO: 나중에 이걸 Enum으로 바꿔보자. 근데, 바꾸는 이유가 중요하겠지? 이유도 찾아보자.

@Getter
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String authId;
    private final String userName;
    private final String userEmail;
    private final String userPicture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String authId, String userName, String userEmail, String userPicture) {
        this.attributes = attributes;
        this.authId = authId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPicture = userPicture;
    }

    public static OAuthAttributes of(String registrationId, Map<String, Object> attributes) {

        switch (registrationId) {
            case "naver":
                return ofNaver(attributes);
            case "kakao":
                return ofKakao(attributes);
            case "github":
                return ofGitHub(attributes);
            case "google":
                return ofGoogle(attributes);
            default:
                throw new IllegalArgumentException("지원하지 않는 로그인 방식입니다.");
        }

    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .authId((String) attributes.get("sub"))
                .userName((String) attributes.get("name"))
                .userEmail((String) attributes.get("email"))
                .userPicture((String) attributes.get("picture"))
                .attributes(attributes)
                .build();
    }


    private static OAuthAttributes ofGitHub(Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .authId(String.valueOf(attributes.get("id")))
                .userName((String) attributes.get("name"))
                .userEmail((String) attributes.get("login"))  // github의 경우, email이 없을 수도 있기 때문에 "login"을 고유 식별자로 사용한다.
                .userPicture((String) attributes.get("avatar_url"))
                .attributes(attributes)
                .build();
    }

    private static OAuthAttributes ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .authId((String) response.get("id"))
                .userName((String) response.get("name"))
                .userEmail((String) response.get("email"))
                .userPicture((String) response.get("profile_image"))
                .attributes(response)
                .build();
    }

    private static OAuthAttributes ofKakao(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) response.get("profile");

        return OAuthAttributes.builder()
                .authId(String.valueOf(attributes.get("id")))
                .userName((String) profile.get("nickname"))
                .userEmail((String) response.get("email"))
                .userPicture((String) profile.get("profile_image_url"))
                .attributes(attributes)
                .build();
    }

    // Map으로 바꿔주는 작업을 해야함. 그래야 DefaultOAuth2User를 똑같은 attribute key를 가진 객체로 바꿀 수 있음
    // Missing attribute 'sub' in attributes -> "sub"이라는 키의 attribute가 있어야 하는듯 ?
    public Map<String, Object> parsedAttributes() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", authId);
        map.put("sub", userEmail);
        map.put("name", userName);
        map.put("picture", userPicture);

        return map;
    }

}
