package com.example.siderswebapp.web.request.create;

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
    private List<CreatedTechStackRequest> stacks;

    @Builder
    public CreateFieldsRequest(String fieldsName, Integer recruitCount, Integer totalAbility, List<CreatedTechStackRequest> stacks) {
        this.fieldsName = fieldsName;
        this.recruitCount = recruitCount;
        this.totalAbility = totalAbility;
        this.stacks = stacks;
    }
}
