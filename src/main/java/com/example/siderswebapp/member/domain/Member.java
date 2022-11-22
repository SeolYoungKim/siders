package com.example.siderswebapp.member.domain;

import static javax.persistence.CascadeType.ALL;

import com.example.siderswebapp.BaseTimeEntity;
import com.example.siderswebapp.post.domain.Post;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String authId;  //TODO: 이런걸 값 타입을 써보는게 어떨까? @Embedded, @Embeddable..!

    @Column
    private String email;  //TODO: 이런걸 값 타입을 써보는게 어떨까? @Embedded, @Embeddable..!

    @Column(name = "member_name")
    private String name;  //TODO: 이런걸 값 타입을 써보는게 어떨까? @Embedded, @Embeddable..!

    @Column
    private String picture;

    @Column
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Column
    private String refreshToken;

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

    public boolean isSameMember(String authId) {
        return this.authId.equals(authId);
    }

    public String getRoleTypeKey() {
        return roleType.getKey();
    }

    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void removePost(Post post) {
        postList.remove(post);
    }
}
