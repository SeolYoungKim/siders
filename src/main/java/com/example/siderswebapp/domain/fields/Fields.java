package com.example.siderswebapp.domain.fields;

import com.example.siderswebapp.domain.Ability;
import com.example.siderswebapp.domain.BaseTimeEntity;
import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.domain.tech_stack.TechStack;
import com.example.siderswebapp.web.request.update.UpdateFieldsRequest;
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
public class Fields extends BaseTimeEntity {

    @Id @Column(name = "fields_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String fieldsName;

    @Column
    private Integer recruitCount;

    @Column
    private Ability totalAbility;

    @JoinColumn(name = "post_id")
    @ManyToOne(fetch = LAZY)
    private Post post;

    @OneToMany(mappedBy = "fields", cascade = ALL, orphanRemoval = true)
    private final List<TechStack> stacks = new ArrayList<>();

    @Builder
    public Fields(String fieldsName, Integer recruitCount, Ability totalAbility, Post post) {
        this.fieldsName = fieldsName;
        this.recruitCount = recruitCount;
        this.totalAbility = totalAbility;
        this.post = post;

        //이거 되나
        this.post.addFields(this);  // 이 때 post에 한번 넘어감 (모두 null인게)
    }

    public void addStack(TechStack stack) {
        stacks.add(stack);
    }

    public void updateFields(UpdateFieldsRequest fieldsDto) {
        this.fieldsName
                = fieldsDto.getFieldsName() != null ? fieldsDto.getFieldsName() : fieldsName;

        this.recruitCount
                = fieldsDto.getRecruitCount() != null ? fieldsDto.getRecruitCount() : recruitCount;

        this.totalAbility
                = fieldsDto.getTotalAbility() != null ? fieldsDto.totalAbilityToEnum() : totalAbility;

    }

}
