package com.example.siderswebapp.domain;

import lombok.Getter;

import java.util.List;

import static java.util.List.*;

/**
 * 모집 분야 및 기술 스택 : 글 작성 및 수정 시 선택 창에 나타낼 용도
 */
@Getter
public enum FieldsType {
    DESIGN("디자인", of("Figma", "Zeplin", "Adobe")),
    FRONTEND("프론트엔드", of("Vue.js", "Angular.js", "React", "JavaScript")),
    BACKEND("백엔드", of("Java", "SpringBoot", "JPA", "QueryDsl")),
    DEVOPS("데브옵스", of("AWS", "Docker", "Kubernetes"));

    private final String key;
    private final List<String> techStack;

    FieldsType(String key, List<String> techStack) {
        this.key = key;
        this.techStack = techStack;
    }
}
