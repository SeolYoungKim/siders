package com.example.siderswebapp.auth.oauth.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.example.siderswebapp.auth.oauth.service.AttributeKeys.*;

/**
 * 일급 컬렉션
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuth2Attributes {

    private final Map<String, Object> attributes = new HashMap<>();

    public OAuth2Attributes(CommonAttributes commonAttributes) {
        attributes.put(ID, commonAttributes.getAuthId());
        attributes.put(SUB, commonAttributes.getEmail());
        attributes.put(NAME, commonAttributes.getName());
        attributes.put(PICTURE, commonAttributes.getPicture());
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }
}
