package com.example.siderswebapp.web.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateFieldsRequest {
    @NotBlank
    private String fieldsName;

    @NotBlank
    private Integer recruitCount;

    @NotBlank
    private Integer totalAbility;

    @NotBlank
    private List<CreatedTechStackRequest> techStackRequests;

    @Builder
    public CreateFieldsRequest(String fieldsName, Integer recruitCount, Integer totalAbility, List<CreatedTechStackRequest> techStackRequests) {
        this.fieldsName = fieldsName;
        this.recruitCount = recruitCount;
        this.totalAbility = totalAbility;
        this.techStackRequests = techStackRequests;
    }
}
