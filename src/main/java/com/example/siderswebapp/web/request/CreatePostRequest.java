package com.example.siderswebapp.web.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@Getter
public class CreatePostRequest {
    @NotBlank
    private final String title;

    @NotBlank
    private final String recruitType;

    @NotBlank
    private final Integer memberCount;

    @NotBlank
    private final String contact;

    @NotBlank
    private final String recruitContent;

    // 필드 정보
    @NotBlank
    private final String fieldsName;

    // 기술 스택 정보 (스택 : 능력치)
    @NotBlank
    private final Map<String, String> stackNamesAndAbilities = new HashMap<>();

    @Builder
    public CreatePostRequest(String title, String recruitType, Integer memberCount, String contact, String recruitContent, String fieldsName) {
        this.title = title;
        this.recruitType = recruitType;
        this.memberCount = memberCount;
        this.contact = contact;
        this.recruitContent = recruitContent;
        this.fieldsName = fieldsName;
    }
}
