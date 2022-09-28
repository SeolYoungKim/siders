package com.example.siderswebapp.web.response;

import com.example.siderswebapp.domain.fields.Fields;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FieldsResponse {
    private final Long id;
    private final String fieldsName;
    private final Integer recruitCount;
    private final String totalAbility;
    private final List<TechStackResponse> stacks;

    @Builder
    public FieldsResponse(Fields fields) {
        this.id = fields.getId();
        this.fieldsName = fields.getFieldsName();
        this.recruitCount = fields.getRecruitCount();
        this.totalAbility = fields.getTotalAbility().name();
        this.stacks = fields.getStacks().stream()
                .map(TechStackResponse::new)
                .collect(Collectors.toList());
    }
}
