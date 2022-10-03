package com.example.siderswebapp.web.response.member;

import lombok.Getter;

@Getter
public class DuplicateNameCheckDto {

    private final Boolean isExists;

    public DuplicateNameCheckDto(Boolean isExists) {
        this.isExists = isExists;
    }
}
