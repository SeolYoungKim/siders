package com.example.siderswebapp.domain.tech_stack;

import com.example.siderswebapp.domain.fields.DevOps;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;

@Getter
@Entity(name = "tech_stack_devops")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DevOpsStack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String stackName;

    @Column(name = "devops_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = ALL)
    private DevOps devOps;

    @Builder
    public DevOpsStack(String stackName, DevOps devOps) {
        this.stackName = stackName;
        this.devOps = devOps;
    }
}
