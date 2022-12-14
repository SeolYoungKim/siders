package com.example.siderswebapp.post.application.dto.create;

import com.example.siderswebapp.post.domain.Ability;
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
    @NotBlank(message = "필드명을 입력해주세요.")
    private String fieldsName;

    @NotNull(message = "모집 인원을 입력해주세요.")
    private Integer recruitCount;

    @NotNull(message = "종합 요구 능력치를 입력해주세요.")
    private String totalAbility;

    @Valid
    @NotEmpty(message = "기술 스택은 1개 이상 선택해야 합니다.")
    private List<CreateTechStackRequest> stacks;

    @Builder
    public CreateFieldsRequest(String fieldsName, Integer recruitCount, String totalAbility,
            List<CreateTechStackRequest> stacks) {
        this.fieldsName = fieldsName;
        this.recruitCount = recruitCount;
        this.totalAbility = totalAbility;
        this.stacks = stacks;
    }

    public Ability totalAbilityToEnum() {
        return Ability.valueOf(totalAbility.toUpperCase());
    }
}
