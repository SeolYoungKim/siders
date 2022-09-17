package com.example.siderswebapp.web.request.create;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateFieldsRequest {
    @NotBlank
    private String fieldsName;

    @NotNull
    private Integer recruitCount;

    @NotNull
    private Integer totalAbility;

    @Valid
    @NotEmpty(message = "기술 스택은 1개 이상 입력되어야 합니다.")
    private List<CreatedTechStackRequest> stacks;

    @Builder
    public CreateFieldsRequest(String fieldsName, Integer recruitCount, Integer totalAbility, List<CreatedTechStackRequest> stacks) {
        this.fieldsName = fieldsName;
        this.recruitCount = recruitCount;
        this.totalAbility = totalAbility;
        this.stacks = stacks;
    }
}
