package com.example.siderswebapp.domain.post;

import com.example.siderswebapp.domain.BaseTimeEntity;
import com.example.siderswebapp.domain.RecruitType;
import com.example.siderswebapp.domain.fields.BackEnd;
import com.example.siderswebapp.domain.fields.Design;
import com.example.siderswebapp.domain.fields.DevOps;
import com.example.siderswebapp.domain.fields.FrontEnd;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

@Getter
@Entity(name = "recruit_post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    @Enumerated(value = EnumType.STRING)
    private RecruitType recruitType;

    @Column
    private String contact;

    @Column
    private String recruitIntroduction;

    @Column(name = "design_id")
    @OneToOne(fetch = LAZY, cascade = ALL) // 글이랑 같이하는 정보라서 CRUD 함께해야함.
    private Design design;

    @Column(name = "frontEnd_id")
    @OneToOne(fetch = LAZY, cascade = ALL)
    private FrontEnd frontEnd;

    @Column(name = "backEnd_id")
    @OneToOne(fetch = LAZY, cascade = ALL)
    private BackEnd backEnd;

    @Column(name = "devops_id")
    @OneToOne(fetch = LAZY, cascade = ALL)
    private DevOps devOps;

    @Builder
    public Post(String title, RecruitType recruitType, String contact, String recruitIntroduction, Design design, FrontEnd frontEnd, BackEnd backEnd, DevOps devOps) {
        this.title = title;
        this.recruitType = recruitType;
        this.contact = contact;
        this.recruitIntroduction = recruitIntroduction;
        this.design = design;
        this.frontEnd = frontEnd;
        this.backEnd = backEnd;
        this.devOps = devOps;
    }
}
