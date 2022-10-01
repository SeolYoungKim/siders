package com.example.siderswebapp.web.request.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignUpDto {

    private String name;

    public SignUpDto(String name) {
        this.name = name;
    }
}
