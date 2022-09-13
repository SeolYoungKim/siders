package com.example.siderswebapp.domain.fields;

import com.example.siderswebapp.domain.tech_stack.BackEndStack;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BackEnd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long recruitCount;

    @Column
    private Long totalAbility;

    @Column
    @OneToMany(mappedBy = "backend", cascade = ALL)
    private final List<BackEndStack> stacks = new ArrayList<>();

    @Builder
    public BackEnd(Long recruitCount, Long totalAbility) {
        this.recruitCount = recruitCount;
        this.totalAbility = totalAbility;
    }

    public void addStack(BackEndStack stack) {
        stacks.add(stack);
    }

}
