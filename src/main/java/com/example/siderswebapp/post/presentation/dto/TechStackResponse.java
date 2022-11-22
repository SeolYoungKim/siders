package com.example.siderswebapp.post.presentation.dto;

import com.example.siderswebapp.post.domain.TechStack;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TechStackResponse {
    private final Long id;
    private final String stackName;

    @Builder
    public TechStackResponse(TechStack techStack) {
        this.id = techStack.getId();
        this.stackName = techStack.getStackName();
    }
}
