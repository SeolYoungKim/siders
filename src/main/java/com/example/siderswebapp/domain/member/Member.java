package com.example.siderswebapp.domain.member;

import com.example.siderswebapp.domain.BaseTimeEntity;
import com.example.siderswebapp.domain.post.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @Column(name = "member_id")
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
    private String refreshToken;

    //TODO: 글이랑 다대일 매핑 해야됨!
    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    private final List<Post> postList = new ArrayList<>();

    @Builder
    public Member(String authId, String email, String name, String picture, RoleType roleType, String refreshToken) {
        this.authId = authId;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.roleType = roleType;
        this.refreshToken = refreshToken;
    }

    public void addPost(Post post) {
        postList.add(post);
    }

    public String getRoleTypeKey() {
        return roleType.getKey();
    }

    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
