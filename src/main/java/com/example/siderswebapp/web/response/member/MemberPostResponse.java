package com.example.siderswebapp.web.response.member;

import com.example.siderswebapp.domain.member.Member;
import com.example.siderswebapp.web.response.post.read.paging.PagingPostsResponse;
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
