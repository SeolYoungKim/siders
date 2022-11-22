package com.example.siderswebapp.member.presentation.dto;

import com.example.siderswebapp.member.domain.Member;
import com.example.siderswebapp.post.presentation.dto.read.paging.PagingPostsResponse;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MemberPostResponse {

    private final List<PagingPostsResponse> memberPostList;

    public MemberPostResponse(Member member) {
        this.memberPostList = member.getPostList().stream()
                .map(PagingPostsResponse::new)
                .collect(Collectors.toList());
    }
}
