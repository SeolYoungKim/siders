package com.example.siderswebapp.auth.oauth.service;

import java.util.Arrays;
import java.util.Map;

import static com.example.siderswebapp.auth.oauth.service.ProviderInfo.values;

/**
 * TODO: 팩토리 인터페이스화 생각
 * resistrationId와 attributes로 CommonAttributes를 만들어준다.
 * CommonAttributes를 이용해서 일급 컬렉션인 OAuth2Attributes를 만들어주는 로직이다.
 */
public class OAuth2AttributesFactory {

    public static OAuth2Attributes getOAuth2Attributes(String registrationId, Map<String, Object> attributes) {
        //resistrationId로 provider를 찾는 게 필요하고
        ProviderInfo providerInfo = getProviderInfo(registrationId);

        //provider를 찾았으면, Provider마다 알맞게 처리하여 commonAttributes를 만들어주는 게 필요함.
        return new OAuth2Attributes(CommonAttributesFactory.getCommonAttributes(providerInfo, attributes));
    }

    // provider를 찾는다.
    private static ProviderInfo getProviderInfo(String registrationId) {
        return Arrays.stream(values())
                .filter(keys -> keys.getRegistrationId().equals(registrationId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 로그인 방식입니다."));
    }
}
