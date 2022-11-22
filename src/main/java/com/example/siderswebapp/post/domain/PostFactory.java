package com.example.siderswebapp.post.domain;

import com.example.siderswebapp.member.domain.Member;
import com.example.siderswebapp.post.application.dto.create.CreatePostRequest;

public class PostFactory {

    public static Post newInstance(CreatePostRequest postDto, Member member) {
        return Post.builder()
                .title(postDto.getTitle())
                .recruitType(postDto.recruitTypeToEnum())
                .contact(postDto.getContact())
                .recruitIntroduction(postDto.getRecruitIntroduction())
                .expectedPeriod(postDto.getExpectedPeriod())
                .member(member)
                .isCompleted(false)
                .build();
    }
}
