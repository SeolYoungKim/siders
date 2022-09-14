package com.example.siderswebapp.web.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter @Setter
public class CreateFieldsRequest {
    @NotBlank
    private final String fieldsName;

    @NotBlank
    private final Integer recruitCount;

    @NotBlank
    private final Integer totalAbility;

    @NotBlank
    private final List<CreatedTechStackRequest> techStackRequests;

    @Builder
    public CreateFieldsRequest(String fieldsName, Integer recruitCount, Integer totalAbility, List<CreatedTechStackRequest> techStackRequests) {
        this.fieldsName = fieldsName;
        this.recruitCount = recruitCount;
        this.totalAbility = totalAbility;
        this.techStackRequests = techStackRequests;
    }
}
