package com.example.siderswebapp.web.response.post.read.paging;

import com.example.siderswebapp.domain.tech_stack.TechStack;
import lombok.Getter;

@Getter
public class PagingTechStackResponse {

    private final Long id;
    private final String stackName;

    public PagingTechStackResponse(TechStack techStack) {
        this.id = techStack.getId();
        this.stackName = techStack.getStackName();
    }
}
