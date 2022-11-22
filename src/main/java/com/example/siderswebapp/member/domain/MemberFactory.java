package com.example.siderswebapp.member.domain;

import com.example.siderswebapp.member.application.dto.SignUpDto;

public class MemberFactory {

    public static Member newInstance(SignUpDto signUpDto, String refreshToken, String authId) {
        return Member.builder()
                .name(signUpDto.getName())
                .authId(authId)
                .email("")
                .picture("")
                .roleType(RoleType.USER)
                .refreshToken(refreshToken)
                .build();
    }

    public static Member guestMember() {
        return Member.builder()
                .roleType(RoleType.GUEST)
                .build();
    }
}
