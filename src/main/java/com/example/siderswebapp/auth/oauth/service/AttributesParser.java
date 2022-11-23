package com.example.siderswebapp.auth.oauth.service;

import java.util.Map;

public class AttributesParser {

    public static Map<String, Object> parseToOAuth2Attributes(String registrationId, Map<String, Object> attributes) {
        OAuth2Attributes oauth2Attributes = new OAuth2Attributes(
                OAuth2ProviderInfo.of(registrationId),
                attributes);

        return oauth2Attributes.parseToCommonAttributes();
    }
}
