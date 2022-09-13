package com.example.siderswebapp.domain.post;

import com.example.siderswebapp.domain.RecruitType;
import com.example.siderswebapp.domain.fileds.Fields;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name = "recruit_post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    @Enumerated(value = EnumType.STRING)
    private RecruitType recruitType;

    @Column
    private Integer memberCount;

    @Column
    private String contact;

    @Column
    private String recruitContent;

    @Column
    @OneToMany(mappedBy = "post")
    private List<Fields> fields = new ArrayList<>();

    @Builder
    public Post(String title, RecruitType recruitType, Integer memberCount, String contact, String recruitContent, ArrayList<Fields> fields) {
        this.title = title;
        this.recruitType = recruitType;
        this.memberCount = memberCount;
        this.contact = contact;
        this.recruitContent = recruitContent;
        this.fields = fields;
    }

    public void addField(Fields fields) {
        this.fields.add(fields);
    }
}
