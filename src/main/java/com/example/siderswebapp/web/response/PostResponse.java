package com.example.siderswebapp.web.response;

import com.example.siderswebapp.domain.post.Post;
import lombok.Builder;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String recruitType;
    private final String contact;
    private final String recruitIntroduction;
    private final String expectedPeriod;
    private final Boolean isCompleted;
    private final String createdDate;
    private final String modifiedDate;
    private final List<FieldsResponse> fieldsList;

    @Builder
    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.recruitType = post.getRecruitType().name();
        this.contact = post.getContact();
        this.recruitIntroduction = post.getRecruitIntroduction();
        this.expectedPeriod = post.getExpectedPeriod();
        this.isCompleted = post.getIsCompleted();
        this.createdDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.modifiedDate = post.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.fieldsList = post.getFieldsList().stream()
                .map(FieldsResponse::new)
                .collect(Collectors.toList());
    }
}
