package com.example.siderswebapp.web.response.member;

import com.example.siderswebapp.domain.member.Member;
import lombok.Getter;

@Getter
public class AuthMemberResponse {

    private final String authId;
    private final String name;
    private final String picture;

    public AuthMemberResponse(Member member) {
        this.authId = member.getAuthId();
        this.name = member.getName();
        this.picture = member.getPicture();
    }
}
