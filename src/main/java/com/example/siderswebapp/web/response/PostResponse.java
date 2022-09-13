package com.example.siderswebapp.web.response;

import com.example.siderswebapp.domain.post.Post;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String recruitType;
    private final Integer memberCount;
    private final String contact;
    private final String recruitContent;
    private final List<FieldsResponse> fields;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.recruitType = post.getRecruitType().getValue();
        this.memberCount = post.getMemberCount();
        this.contact = post.getContact();
        this.recruitContent = post.getRecruitContent();
        this.fields = post.getFields().stream()
                .map(FieldsResponse::new)
                .collect(Collectors.toList());
    }
}
