package com.example.siderswebapp.domain.tech_stack;

import com.example.siderswebapp.domain.fileds.Fields;
import com.example.siderswebapp.domain.stack_ability.StackAbility;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity(name = "tech_stack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String stackName;

    @Column
    @ManyToOne(fetch = FetchType.LAZY)
    private Fields fields;

    @Column
    @OneToOne(fetch = FetchType.LAZY)
    private StackAbility stackAbility;

    @Builder
    public TechStack(String stackName, Fields fields, StackAbility stackAbility) {
        this.stackName = stackName;
        this.fields = fields;
        this.stackAbility = stackAbility;
    }
}
