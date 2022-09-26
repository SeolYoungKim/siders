package com.example.siderswebapp.domain.member;

import com.example.siderswebapp.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true)
    private String nickName;

    private String profileImg;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String refreshToken;

    @Builder
    public Member(String email, String nickName, String profileImg, Role role, String refreshToken) {
        this.email = email;
        this.nickName = nickName;
        this.profileImg = profileImg;
        this.role = role;
        this.refreshToken = refreshToken;
    }

    public Member update(String profileImg) {
        this.profileImg = profileImg;
        return this;
    }

    public String getRoleKey() {
        return role.getKey();
    }
}
