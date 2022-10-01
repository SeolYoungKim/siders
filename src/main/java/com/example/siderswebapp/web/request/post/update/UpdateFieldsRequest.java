package com.example.siderswebapp.web.request.post.update;

import com.example.siderswebapp.domain.Ability;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateFieldsRequest {

    private Long id;

    @NotBlank(message = "필드명을 입력해주세요.")
    private String fieldsName;

    @NotNull(message = "모집 인원을 입력해주세요.")
    private Integer recruitCount;

    @NotNull(message = "종합 요구 능력치를 입력해주세요.")
    private String totalAbility;

    private Boolean isDelete;

    @Valid
    @NotEmpty(message = "기술 스택은 1개 이상 선택해야 합니다.")
    private List<UpdateTechStackRequest> stacks;

    @Builder
    public UpdateFieldsRequest(Long id, String fieldsName, Integer recruitCount, String totalAbility, Boolean isDelete,List<UpdateTechStackRequest> stacks) {
        this.id = id;
        this.fieldsName = fieldsName;
        this.recruitCount = recruitCount;
        this.totalAbility = totalAbility;
        this.stacks = stacks;
        this.isDelete = isDelete;
    }

    public Ability totalAbilityToEnum() {
        return Arrays.stream(Ability.values())
                .filter(ability -> ability.name().equals(totalAbility.toUpperCase()))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("종합 요구 능력치가 누락되었습니다."));
    }
}
