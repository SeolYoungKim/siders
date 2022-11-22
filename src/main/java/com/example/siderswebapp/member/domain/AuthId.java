package com.example.siderswebapp.member.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthId {

    @Column(name = "auth_id",nullable = false, unique = true)
    private String value;

    public AuthId(final String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthId authId = (AuthId) o;
        return Objects.equals(value, authId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public boolean isSameMember(String authId) {
        return value.equals(authId);
    }
}
