package com.example.siderswebapp.auth.oauth.service;

import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

import static com.example.siderswebapp.auth.oauth.service.AttributeKeys.*;

/**
 * 각각 다른 곳에서 로그인을 요청한 유저의 attributes 정보를 평준화 하는 클래스
 */

@Deprecated
@SuppressWarnings({"unchecked"})
public class OAuthAttributesOld {

    private Map<String, Object> attributes;
    private String authId;
    private String userName;
    private String userEmail;
    private String userPicture;

    private OAuthAttributesOld() {
    }

    @Builder
    private OAuthAttributesOld(Map<String, Object> attributes, String authId, String userName,
                               String userEmail, String userPicture) {
        this.attributes = attributes;
        this.authId = authId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPicture = userPicture;
    }

    public static OAuthAttributesOld of(String registrationId, Map<String, Object> attributes) {

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

    private static OAuthAttributesOld ofGoogle(Map<String, Object> attributes) {
        return OAuthAttributesOld.builder()
                .authId((String) attributes.get("sub"))
                .userName((String) attributes.get("name"))
                .userEmail((String) attributes.get("email"))
                .userPicture((String) attributes.get("picture"))
                .attributes(attributes)
                .build();
    }


    private static OAuthAttributesOld ofGitHub(Map<String, Object> attributes) {
        return OAuthAttributesOld.builder()
                .authId(String.valueOf(attributes.get("id")))
                .userName((String) attributes.get("name"))
                .userEmail((String) attributes.get("login"))  // github의 경우, email이 없을 수도 있기 때문에 "login"을 고유 식별자로 사용한다.
                .userPicture((String) attributes.get("avatar_url"))
                .attributes(attributes)
                .build();
    }

    private static OAuthAttributesOld ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributesOld.builder()
                .authId((String) response.get("id"))
                .userName((String) response.get("name"))
                .userEmail((String) response.get("email"))
                .userPicture((String) response.get("profile_image"))
                .attributes(response)
                .build();
    }

    private static OAuthAttributesOld ofKakao(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) response.get("profile");

        return OAuthAttributesOld.builder()
                .authId(String.valueOf(attributes.get("id")))
                .userName((String) profile.get("nickname"))
                .userEmail((String) response.get("email"))
                .userPicture((String) profile.get("profile_image_url"))
                .attributes(attributes)
                .build();
    }

    // Map으로 바꿔주는 작업을 해야함. 그래야 DefaultOAuth2User를 똑같은 attribute key를 가진 객체로 바꿀 수 있음
    // Missing attribute 'sub' in attributes -> "sub"이라는 키의 attribute가 있어야 하는듯 ?
    // TODO: 여기에 일급 컬렉션을 적용하는 게 좋은 선택일까?
    public Map<String, Object> parsedAttributes() {
        Map<String, Object> map = new HashMap<>();
        map.put(ID, authId);
        map.put(SUB, userEmail);
        map.put(NAME, userName);
        map.put(PICTURE, userPicture);

        return map;
    }

}
