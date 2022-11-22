package com.example.siderswebapp.post.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import com.example.siderswebapp.BaseTimeEntity;
import com.example.siderswebapp.post.application.dto.update.UpdateFieldsRequest;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Fields extends BaseTimeEntity {

    @Id @Column(name = "fields_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: 공통적인 부분은 테이블을 따로 빼도 될까?
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

    @Builder  //TODO public -> default
    public Fields(String fieldsName, Integer recruitCount, Ability totalAbility, Post post) {
        this.fieldsName = fieldsName;
        this.recruitCount = recruitCount;
        this.totalAbility = totalAbility;
        this.post = post;

        this.post.addFields(this);
    }

    public void addStack(TechStack stack) {
        stacks.add(stack);
    }

    public void updateFields(UpdateFieldsRequest fieldsDto) {
        this.fieldsName = updateValue(
                fieldsDto.getFieldsName(), fieldsName);

        this.recruitCount = updateValue(
                fieldsDto.getRecruitCount(), recruitCount);

        this.totalAbility = updateValue(
                fieldsDto.totalAbilityToEnum(), totalAbility);

        clearStacks();
    }

    private <T> T updateValue(T dtoValue, T entityValue) {
        if (dtoValue == null) {
            return entityValue;
        }

        return dtoValue;
    }

    private void clearStacks() {
        stacks.clear();
    }

}
