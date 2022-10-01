package com.example.siderswebapp.domain.member;

import com.example.siderswebapp.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String authId;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String picture;

    @Column
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column
    private String accessToken;

    @Column
    private String refreshToken;

    @Builder
    public Member(String authId, String email, String name, String picture, RoleType roleType, String accessToken, String refreshToken) {
        this.authId = authId;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.roleType = roleType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getRoleTypeKey() {
        return roleType.getKey();
    }

    public void saveToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
