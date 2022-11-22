package com.example.siderswebapp.member.presentation.dto;

import com.example.siderswebapp.member.domain.Member;
import lombok.Getter;

@Getter
public class AuthMemberResponse {

    private final String authId;
    private final String name;
    private final String picture;
    private final Boolean isAuthMember;

    public AuthMemberResponse(Member member) {
        boolean isAuthMember = member != null;

        this.authId = isAuthMember ? member.getAuthId() : null;
        this.name = isAuthMember ? member.getName() : null;
        this.picture = isAuthMember ? member.getPicture() : null;
        this.isAuthMember = isAuthMember;
    }
}
