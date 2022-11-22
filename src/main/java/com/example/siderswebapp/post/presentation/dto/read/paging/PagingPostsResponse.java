package com.example.siderswebapp.post.presentation.dto.read.paging;

import com.example.siderswebapp.post.domain.Post;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PagingPostsResponse {

    private final Long id;
    private final String title;
    private final String recruitType;
    private final String createdDate;
    private final String modifiedDate;
    private final List<PagingFieldsResponse> fieldsList;

    public PagingPostsResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.recruitType = post.getRecruitType().name();
        this.createdDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.modifiedDate = post.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.fieldsList = post.getFieldsList().stream()
                .map(PagingFieldsResponse::new)
                .collect(Collectors.toList());
    }
}
