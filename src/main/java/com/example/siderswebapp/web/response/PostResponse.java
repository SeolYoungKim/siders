package com.example.siderswebapp.web.response;

import com.example.siderswebapp.domain.post.Post;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String recruitType;
    private final String contact;
    private final String recruitIntroduction;
    private final List<FieldsResponse> fieldsList;

    @Builder

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.recruitType = post.getRecruitType().name();
        this.contact = post.getContact();
        this.recruitIntroduction = post.getRecruitIntroduction();
        this.fieldsList = post.getFieldsList().stream()
                .map(FieldsResponse::new)
                .collect(Collectors.toList());
    }
}
