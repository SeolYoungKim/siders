package com.example.siderswebapp.web.response.member;

import com.example.siderswebapp.domain.member.Member;
import lombok.Getter;

@Getter
public class SignUpMemberResponse {

    private final String authId;
    private final String name;
    private final String picture;
    private final String accessToken;

    public SignUpMemberResponse(Member member, String accessToken) {
        this.authId = member.getAuthId();
        this.name = member.getName();
        this.picture = member.getPicture();
        this.accessToken = accessToken;
    }
}
