package com.example.siderswebapp.domain.tech_stack;

import com.example.siderswebapp.domain.fields.Design;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;

@Getter
@Entity(name = "tech_stack_design")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DesignStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String stackName;

    @Column(name = "design_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = ALL)
    private Design design;

    @Builder
    public DesignStack(String stackName, Design design) {
        this.stackName = stackName;
        this.design = design;
    }
}
