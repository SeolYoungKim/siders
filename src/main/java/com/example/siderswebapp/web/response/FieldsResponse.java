package com.example.siderswebapp.web.response;

import com.example.siderswebapp.domain.fileds.Fields;
import lombok.Builder;
import lombok.Getter;

@Getter
public class FieldsResponse {

    private final Long id;
    private final String fieldsName;

    @Builder
    public FieldsResponse(Fields fields) {
        this.id = fields.getId();
        this.fieldsName = fields.getFieldsName();
    }
}
