package com.example.siderswebapp.domain.tech_stack;

import com.example.siderswebapp.domain.fields.FrontEnd;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;

@Getter
@Entity(name = "tech_stack_frontend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FrontEndStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String stackName;

    @Column(name = "frontend_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = ALL)
    private FrontEnd frontend;

    @Builder
    public FrontEndStack(String stackName, FrontEnd frontend) {
        this.stackName = stackName;
        this.frontend = frontend;
    }
}
