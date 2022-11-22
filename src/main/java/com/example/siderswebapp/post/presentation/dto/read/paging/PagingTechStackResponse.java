package com.example.siderswebapp.post.presentation.dto.read.paging;

import com.example.siderswebapp.post.domain.TechStack;
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
