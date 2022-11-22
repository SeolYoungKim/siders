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
        this.authId = member.getAuthId().getValue();
        this.name = member.getName().getValue();
        this.picture = member.getPicture();
        this.isAuthMember = member.getRoleType().isNotGuest();
    }
}
