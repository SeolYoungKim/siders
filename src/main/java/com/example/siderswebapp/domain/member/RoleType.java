package com.example.siderswebapp.domain.member;

import lombok.Getter;

@Getter
public enum RoleType {
    GUEST("ROLE_GUEST"), USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

    private final String key;

    RoleType(String key) {
        this.key = key;
    }
}
