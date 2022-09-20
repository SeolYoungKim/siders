package com.example.siderswebapp.config.oauth;

import com.example.siderswebapp.domain.member.Member;
import com.example.siderswebapp.domain.member.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class OAuthAttributes {

    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String email;
    private final String nickName;
    private final String profileImg;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey,
                           String email, String nickName, String profileImg) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.email = email;
        this.nickName = nickName;
        this.profileImg = profileImg;
    }

    public static OAuthAttributes of(String registrationId,
                                     String userNameAttributeName, Map<String, Object> attributes) {

        switch (registrationId) {
            case "naver":
                return ofNaver("id", attributes);
            case "kakao":
                return ofKakao("id", attributes);
            case "github":
                return ofGithub(userNameAttributeName, attributes);
            case "google":
                return ofGoogle(userNameAttributeName, attributes);
            default:
                throw new IllegalArgumentException("지원하지 않는 인증 제공자입니다.");
        }
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .nickName((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profileImg((String) attributes.get("picture"))
                .build();
    }

    //TODO: attr 확인 후 재설정
    private static OAuthAttributes ofGithub(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .nickName((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .profileImg((String) attributes.get("avatar_url"))
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .nickName((String) response.get("name"))
                .email((String) response.get("email"))
                .profileImg((String) response.get("profile_image"))
                .build();
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) response.get("profile");

        return OAuthAttributes.builder()
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .nickName((String) profile.get("nickname"))
                .email((String) response.get("email"))
                .profileImg((String) profile.get("profile_image_url"))
                .build();
    }

    public Member toEntity() {
        return Member.builder()
                .nickName(nickName)
                .email(email)
                .profileImg(profileImg)
                .role(Role.USER)
                .build();
    }

    public Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", nameAttributeKey);
        map.put("key", nameAttributeKey);
        map.put("name", nickName);
        map.put("email", email);
        map.put("profileImg", profileImg);

        return map;
    }
}
