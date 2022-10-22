package com.example.siderswebapp.auth.oauth.service;

import com.example.siderswebapp.exception.IsNotSupportedOAuth2Login;

import java.util.Map;

import static com.example.siderswebapp.auth.oauth.service.AttributeKeys.*;
import static com.example.siderswebapp.auth.oauth.service.ProviderInfo.*;

@SuppressWarnings({"unchecked"})
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
        Map<String, Object> response = (Map<String, Object>) attributes.get(NAVER_RESPONSE_KEY);

        return CommonAttributes.builder()
                .authId(String.valueOf(response.get(providerInfo.getAuthId())))
                .email(String.valueOf(response.get(providerInfo.getEmail())))
                .name(String.valueOf(response.get(providerInfo.getName())))
                .picture(String.valueOf(response.get(providerInfo.getPicture())))
                .build();
    }

    private static CommonAttributes ofKaKao(ProviderInfo providerInfo, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get(KAKAO_ACCOUNT_KEY);
        Map<String, Object> profile = (Map<String, Object>) response.get(KAKAO_PROFILE_KEY);

        return CommonAttributes.builder()
                .authId(String.valueOf(attributes.get(providerInfo.getAuthId())))
                .email(String.valueOf(profile.get(providerInfo.getEmail())))
                .name(String.valueOf(response.get(providerInfo.getName())))
                .picture(String.valueOf(profile.get(providerInfo.getPicture())))
                .build();
    }
}
