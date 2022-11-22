package com.example.siderswebapp.member.presentation.dto;

import lombok.Getter;

@Getter
public class DuplicateNameCheckDto {

    private final Boolean isExists;

    public DuplicateNameCheckDto(Boolean isExists) {
        this.isExists = isExists;
    }
}
