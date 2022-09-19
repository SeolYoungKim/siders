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

    // 예상 소요 기간
    @Column
    private String expectedPeriod;

    //모집 완료 여부 -> 이에 따라 글 노출 여부 결정 (true - 글 목록에는 노출X, 마이페이지 노출O | false - 둘 다 노출)
    //TODO: 나중에는 isCompleted를 DB 컬럼 따로 빼서 적용해보자.
    @Column
    private Boolean isCompleted;

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private final List<Fields> fieldsList = new ArrayList<>();

    @Builder
    public Post(String title, RecruitType recruitType, String contact, String recruitIntroduction,
                String expectedPeriod, Boolean isCompleted) {
        this.title = title;
        this.recruitType = recruitType;
        this.contact = contact;
        this.recruitIntroduction = recruitIntroduction;
        this.expectedPeriod = expectedPeriod;
        this.isCompleted = isCompleted;
    }

    public void addFields(Fields fields) {
        fieldsList.add(fields);
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

        this.expectedPeriod
                = postDto.getExpectedPeriod() != null ? postDto.getExpectedPeriod() : expectedPeriod;
    }

    public void changeCompletion(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}





