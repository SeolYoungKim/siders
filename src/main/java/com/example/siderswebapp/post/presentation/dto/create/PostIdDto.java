package com.example.siderswebapp.post.presentation.dto.create;

import lombok.Getter;

@Getter
public class PostIdDto {

    private final Long postId;

    public PostIdDto(Long postId) {
        this.postId = postId;
    }
}
