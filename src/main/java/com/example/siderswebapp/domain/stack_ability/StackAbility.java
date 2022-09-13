package com.example.siderswebapp.domain.stack_ability;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity(name = "stack_ability")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StackAbility {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long stat;

    @Builder
    public StackAbility(Long stat) {
        this.stat = stat;
    }
}
