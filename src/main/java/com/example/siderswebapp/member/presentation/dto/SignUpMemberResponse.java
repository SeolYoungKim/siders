package com.example.siderswebapp.member.presentation.dto;

import com.example.siderswebapp.member.domain.Member;
import lombok.Getter;

@Getter
public class SignUpMemberResponse {

    private final String authId;
    private final String name;
    private final String picture;
    private final String accessToken;

    public SignUpMemberResponse(Member member, String accessToken) {
        this.authId = member.getAuthId().getValue();
        this.name = member.getName().getValue();
        this.picture = member.getPicture();
        this.accessToken = accessToken;
    }
}
