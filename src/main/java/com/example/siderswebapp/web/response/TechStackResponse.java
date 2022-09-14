package com.example.siderswebapp.web.response;

import com.example.siderswebapp.domain.tech_stack.TechStack;
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
