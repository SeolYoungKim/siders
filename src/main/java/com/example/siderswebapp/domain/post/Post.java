package com.example.siderswebapp.domain.post;

import com.example.siderswebapp.domain.BaseTimeEntity;
import com.example.siderswebapp.domain.RecruitType;
import com.example.siderswebapp.domain.fields.Fields;
import com.example.siderswebapp.web.request.update.UpdatePostRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

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

    //TODO: 모집 완료 여부 true, false로 넣기. 이걸까먹었네 ㅠ.

    @OneToMany(mappedBy = "post", cascade = ALL)
    private final List<Fields> fieldsList = new ArrayList<>();

    @Builder
    public Post(String title, RecruitType recruitType, String contact, String recruitIntroduction) {
        this.title = title;
        this.recruitType = recruitType;
        this.contact = contact;
        this.recruitIntroduction = recruitIntroduction;
    }

    public void addFields(Fields fields) {
        fieldsList.add(fields);  //여기서 add 해버림..;
    }

    public void updatePost(UpdatePostRequest postDto) {
        this.title
                = postDto.getTitle() != null ? postDto.getTitle() : title;

        this.recruitType
                = postDto.recruitTypeToEnum() != null ? postDto.recruitTypeToEnum() : recruitType;

        this.contact
                = postDto.getContact() != null ? postDto.getContact() : contact;

        this.recruitIntroduction
                = postDto.getRecruitIntroduction() != null ? postDto.getRecruitIntroduction() : recruitIntroduction;
    }
}





