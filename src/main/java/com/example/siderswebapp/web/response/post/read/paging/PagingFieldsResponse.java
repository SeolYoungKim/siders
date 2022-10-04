package com.example.siderswebapp.web.response.post.read.paging;

import com.example.siderswebapp.domain.fields.Fields;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PagingFieldsResponse {

    private final Long id;
    private final String fieldsName;
    private final Integer recruitCount;
    private final List<PagingTechStackResponse> stacks;


    public PagingFieldsResponse(Fields fields) {
        this.id = fields.getId();
        this.fieldsName = fields.getFieldsName();
        this.recruitCount = fields.getRecruitCount();
        this.stacks = fields.getStacks().stream()
                .map(PagingTechStackResponse::new)
                .collect(Collectors.toList());
    }
}
