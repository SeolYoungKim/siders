package com.example.siderswebapp.domain;

import lombok.Getter;

@Getter
public enum RecruitType {
    STUDY("스터디"),
    PROJECT("프로젝트");

    private final String value;

    RecruitType(String value) {
        this.value = value;
    }
}
