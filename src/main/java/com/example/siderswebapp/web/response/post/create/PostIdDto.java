package com.example.siderswebapp.web.response.post.create;

import lombok.Getter;

@Getter
public class PostIdDto {

    private final Long postId;

    public PostIdDto(Long postId) {
        this.postId = postId;
    }
}
