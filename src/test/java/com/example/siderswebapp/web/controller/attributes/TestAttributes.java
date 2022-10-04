package com.example.siderswebapp.web.controller.attributes;

import java.util.HashMap;
import java.util.Map;

public enum TestAttributes {

    TEST_ATTRIBUTES {

        @Override
        public Map<String, Object> getAttributes() {
            Map<String, Object> attr = new HashMap<>();
            attr.put("id", "authId");
            attr.put("sub", "email");
            attr.put("picture", "picture");

            return attr;
        }
    };

    public abstract Map<String, Object> getAttributes();
}
