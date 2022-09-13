package com.example.siderswebapp.domain.fileds;

import com.example.siderswebapp.domain.post.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Fields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fieldsName;

    @Column
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @Builder
    public Fields(String fieldsName, Post post) {
        this.fieldsName = fieldsName;
        this.post = post;
    }
}
