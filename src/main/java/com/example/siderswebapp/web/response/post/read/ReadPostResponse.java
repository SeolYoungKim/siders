package com.example.siderswebapp.web.response.post.read;

import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.web.response.post.FieldsResponse;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO 리팩토링 최우선 대상.......
 */

@Getter
public class ReadPostResponse {
    private final Long id;
    private final String title;
    private final String recruitType;
    private final String contact;
    private final String recruitIntroduction;
    private final String expectedPeriod;
    private final String authId;
    private final Boolean isCompleted;
    private final Boolean isWriter;
    private final String createdDate;
    private final String modifiedDate;
    private final List<FieldsResponse> fieldsList;

    public ReadPostResponse(Post post, Boolean isWriter) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.recruitType = post.getRecruitType().name();
        this.contact = post.getContact();
        this.recruitIntroduction = post.getRecruitIntroduction();
        this.expectedPeriod = post.getExpectedPeriod();
        this.authId = post.getMember().getAuthId();
        this.isCompleted = post.getIsCompleted();
        this.isWriter = isWriter;
        this.createdDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.modifiedDate = post.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.fieldsList = post.getFieldsList().stream()
                .map(FieldsResponse::new)
                .collect(Collectors.toList());
    }
}
