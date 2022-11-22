package com.example.siderswebapp.post.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

import com.example.siderswebapp.BaseTimeEntity;
import com.example.siderswebapp.member.domain.Member;
import com.example.siderswebapp.exception.domain.IsNotOwnerException;
import com.example.siderswebapp.post.application.dto.update.UpdatePostRequest;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;  //TODO: 이런걸 값 타입을 써보는게 어떨까? @Embedded, @Embeddable..!

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

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = LAZY)
    private Member member;

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private final List<Fields> fieldsList = new ArrayList<>();

    @Builder
    public Post(String title, RecruitType recruitType, String contact, String recruitIntroduction,
                String expectedPeriod, Boolean isCompleted, Member member) {
        this.title = title;
        this.recruitType = recruitType;
        this.contact = contact;
        this.recruitIntroduction = recruitIntroduction;
        this.expectedPeriod = expectedPeriod;
        this.isCompleted = isCompleted;
        this.member = member;

        this.member.addPost(this);
    }

    public boolean writtenBy(String authId) {
        return member.isSameMember(authId);
    }

    public void addFields(Fields fields) {
        fieldsList.add(fields);
    }

    public void updatePost(UpdatePostRequest postDto) {
        this.title = updateValue(
                postDto.getTitle(), title);
        this.recruitType = updateValue(
                postDto.recruitTypeToEnum(), recruitType);
        this.contact = updateValue(
                postDto.getContact(), contact);
        this.recruitIntroduction = updateValue(
                postDto.getRecruitIntroduction(), recruitIntroduction);
        this.expectedPeriod = updateValue(
                postDto.getExpectedPeriod(), expectedPeriod);
    }

    public void updatePost(UpdatePostRequest postDto, String authId) {
        if (!member.isSameMember(authId)) {
            throw new IsNotOwnerException();
        }

        this.title = updateValue(
                postDto.getTitle(), title);
        this.recruitType = updateValue(
                postDto.recruitTypeToEnum(), recruitType);
        this.contact = updateValue(
                postDto.getContact(), contact);
        this.recruitIntroduction = updateValue(
                postDto.getRecruitIntroduction(), recruitIntroduction);
        this.expectedPeriod = updateValue(
                postDto.getExpectedPeriod(), expectedPeriod);
    }

    private <T> T updateValue(T dtoValue, T entityValue) {
        if (dtoValue == null) {
            return entityValue;
        }

        return dtoValue;
    }

    public void removeFields(Fields fields) {
        fieldsList.remove(fields);
    }

    public void changeCompletion(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void validateIsWriter(String authId) {
        if (!writtenBy(authId)) {
            throw new IsNotOwnerException();
        }
    }
}





