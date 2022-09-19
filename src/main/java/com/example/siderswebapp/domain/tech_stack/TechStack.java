package com.example.siderswebapp.domain.tech_stack;

import com.example.siderswebapp.domain.fields.Fields;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity(name = "tech_stack")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechStack {

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

        //이거 되나
        this.fields.addStack(this);
    }

//    public void updateTechStack(UpdateTechStackRequest techStackDto) {
//        this.stackName = techStackDto.getStackName() != null ? techStackDto.getStackName() : stackName;
//    }
}
