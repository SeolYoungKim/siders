package com.example.siderswebapp.domain.post;

import com.example.siderswebapp.domain.member.Member;
import com.example.siderswebapp.web.request.post.create.CreatePostRequest;

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
