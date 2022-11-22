package com.example.siderswebapp.auth.oauth.service;

import static com.example.siderswebapp.auth.oauth.service.AttributeKeys.KAKAO_ACCOUNT_KEY;
import static com.example.siderswebapp.auth.oauth.service.AttributeKeys.KAKAO_PROFILE_KEY;
import static com.example.siderswebapp.auth.oauth.service.AttributeKeys.NAVER_RESPONSE_KEY;
import static com.example.siderswebapp.auth.oauth.service.ProviderInfo.GITHUB;
import static com.example.siderswebapp.auth.oauth.service.ProviderInfo.GOOGLE;
import static com.example.siderswebapp.auth.oauth.service.ProviderInfo.KAKAO;
import static com.example.siderswebapp.auth.oauth.service.ProviderInfo.NAVER;

import com.example.siderswebapp.exception.domain.IsNotSupportedOAuth2Login;
import java.util.Map;

public class CommonAttributesFactory {

    // Provider 정보를 받아서 CommonAttributes를 만들어준다.
    public static CommonAttributes getCommonAttributes(ProviderInfo providerInfo, Map<String, Object> attributes) {
        // provider info 에 따라 각각 다른 commonAttributes 생성
        if (providerInfo == GOOGLE || providerInfo == GITHUB) {
            return ofDefaultProvider(providerInfo, attributes);
        } else if (providerInfo == NAVER) {
            return ofNaver(providerInfo, attributes);
        } else if (providerInfo == KAKAO) {
            return ofKaKao(providerInfo, attributes);
        }

        throw new IsNotSupportedOAuth2Login();
    }

    private static CommonAttributes ofDefaultProvider(ProviderInfo providerInfo, Map<String, Object> attributes) {
        return CommonAttributes.builder()
                .authId(String.valueOf(attributes.get(providerInfo.getAuthId())))
                .email(String.valueOf(attributes.get(providerInfo.getEmail())))
                .name(String.valueOf(attributes.get(providerInfo.getName())))
                .picture(String.valueOf(attributes.get(providerInfo.getPicture())))
                .build();
    }

    private static CommonAttributes ofNaver(ProviderInfo providerInfo, Map<String, Object> attributes) {
        @SuppressWarnings({"unchecked"})
        Map<String, Object> response = (Map<String, Object>) attributes.get(NAVER_RESPONSE_KEY);

        return CommonAttributes.builder()
                .authId(String.valueOf(response.get(providerInfo.getAuthId())))
                .email(String.valueOf(response.get(providerInfo.getEmail())))
                .name(String.valueOf(response.get(providerInfo.getName())))
                .picture(String.valueOf(response.get(providerInfo.getPicture())))
                .build();
    }

    private static CommonAttributes ofKaKao(ProviderInfo providerInfo, Map<String, Object> attributes) {
        @SuppressWarnings({"unchecked"})
        Map<String, Object> response = (Map<String, Object>) attributes.get(KAKAO_ACCOUNT_KEY);

        @SuppressWarnings({"unchecked"})
        Map<String, Object> profile = (Map<String, Object>) response.get(KAKAO_PROFILE_KEY);

        return CommonAttributes.builder()
                .authId(String.valueOf(attributes.get(providerInfo.getAuthId())))
                .email(String.valueOf(profile.get(providerInfo.getEmail())))
                .name(String.valueOf(response.get(providerInfo.getName())))
                .picture(String.valueOf(profile.get(providerInfo.getPicture())))
                .build();
    }
}
