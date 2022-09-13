package com.example.siderswebapp.domain.fields;

import com.example.siderswebapp.domain.tech_stack.FrontEndStack;
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
public class FrontEnd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long recruitCount;

    @Column
    private Long totalAbility;

    @Column
    @OneToMany(mappedBy = "frontend", cascade = ALL)
    private final List<FrontEndStack> stacks = new ArrayList<>();

    @Builder
    public FrontEnd(Long recruitCount, Long totalAbility) {
        this.recruitCount = recruitCount;
        this.totalAbility = totalAbility;
    }

    public void addStack(FrontEndStack stack) {
        stacks.add(stack);
    }
}
