package com.example.siderswebapp.post.domain;

import com.example.siderswebapp.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity(name = "tech_stack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechStack extends BaseTimeEntity {

    @Id @Column(name = "tech_stack_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String stackName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fields_id")
    private Fields fields;

    @Builder
    public TechStack(String stackName, Fields fields) {
        this.stackName = stackName;
        this.fields = fields;

        this.fields.addStack(this);
    }

}
