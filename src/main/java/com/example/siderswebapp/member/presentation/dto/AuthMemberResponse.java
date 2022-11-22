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

        this.authId = isAuthMember ? member.getAuthId().getValue() : null;
        this.name = isAuthMember ? member.getName().getValue() : null;
        this.picture = isAuthMember ? member.getPicture() : null;
        this.isAuthMember = isAuthMember;
    }
}
