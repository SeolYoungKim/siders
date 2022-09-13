package com.example.siderswebapp.domain.tech_stack;

import com.example.siderswebapp.domain.fields.BackEnd;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;

@Getter
@Entity(name = "tech_stack_backend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BackEndStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String stackName;

    @Column(name = "backend_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = ALL)
    private BackEnd backend;

    @Builder
    public BackEndStack(String stackName, BackEnd backend) {
        this.stackName = stackName;
        this.backend = backend;
    }
}
