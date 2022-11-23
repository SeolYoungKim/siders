package com.example.siderswebapp.auth.oauth.service;

import static com.example.siderswebapp.auth.oauth.service.AttributeKeys.ID;
import static com.example.siderswebapp.auth.oauth.service.AttributeKeys.KAKAO_ACCOUNT_KEY;
import static com.example.siderswebapp.auth.oauth.service.AttributeKeys.KAKAO_PROFILE_KEY;
import static com.example.siderswebapp.auth.oauth.service.AttributeKeys.NAME;
import static com.example.siderswebapp.auth.oauth.service.AttributeKeys.NAVER_RESPONSE_KEY;
import static com.example.siderswebapp.auth.oauth.service.AttributeKeys.PICTURE;
import static com.example.siderswebapp.auth.oauth.service.AttributeKeys.SUB;

import com.example.siderswebapp.exception.domain.IsNotSupportedOAuth2Login;
import java.util.Map;

public class OAuth2Attributes {

    private final OAuth2ProviderInfo oauth2ProviderInfo;
    private final Map<String, Object> attributes;

    public OAuth2Attributes(OAuth2ProviderInfo oauth2ProviderInfo, Map<String, Object> attributes) {
        this.oauth2ProviderInfo = oauth2ProviderInfo;
        this.attributes = attributes;
    }

    @SuppressWarnings({"unchecked"})
    public Map<String, Object> parseToCommonAttributes() {
        if (oauth2ProviderInfo.isGoogleOrGitHub()) {
            return parseAttributes(attributes, attributes, attributes);

        } else if (oauth2ProviderInfo.isNaver()) {
            Map<String, Object> response = (Map<String, Object>) attributes.get(NAVER_RESPONSE_KEY);
            return parseAttributes(response, response, response);

        } else if (oauth2ProviderInfo.isKakao()) {
            Map<String, Object> account = (Map<String, Object>) attributes.get(KAKAO_ACCOUNT_KEY);
            Map<String, Object> profile = (Map<String, Object>) account.get(KAKAO_PROFILE_KEY);
            return parseAttributes(attributes, account, profile);
        }

        throw new IsNotSupportedOAuth2Login();
    }

    private Map<String, Object> parseAttributes(
            Map<String, Object> attributes,
            Map<String, Object> account,
            Map<String, Object> profile
    ) {
        return Map.of(
                ID, oauth2ProviderInfo.findAuthId(attributes),
                SUB, oauth2ProviderInfo.findEmail(profile),
                NAME, oauth2ProviderInfo.findName(account),
                PICTURE, oauth2ProviderInfo.findPicture(profile));
    }
}
