package com.example.siderswebapp.domain.fields;

import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.domain.tech_stack.TechStack;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.*;

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
    private Integer recruitCount;

    @Column
    private Integer totalAbility;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = LAZY, cascade = ALL)
    private Post post;

    @OneToMany(mappedBy = "fields", cascade = ALL)
    private final List<TechStack> stacks = new ArrayList<>();

    @Builder
    public Fields(String fieldsName, Integer recruitCount, Integer totalAbility, Post post) {
        this.fieldsName = fieldsName;
        this.recruitCount = recruitCount;
        this.totalAbility = totalAbility;
        this.post = post;

        //이거 되나
        this.post.addFields(this);
    }

    public void addStack(TechStack stack) {
        stacks.add(stack);
    }

}