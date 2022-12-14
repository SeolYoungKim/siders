package com.example.siderswebapp.post.application.dto.update;

import com.example.siderswebapp.post.domain.Ability;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

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
    public UpdateFieldsRequest(Long id, String fieldsName, Integer recruitCount,
            String totalAbility, Boolean isDelete, List<UpdateTechStackRequest> stacks) {
        this.id = id;
        this.fieldsName = fieldsName;
        this.recruitCount = recruitCount;
        this.totalAbility = totalAbility;
        this.stacks = stacks;
        this.isDelete = isDelete;
    }

    public Ability totalAbilityToEnum() {
        return Ability.valueOf(totalAbility.toUpperCase());
    }

    public Long getId() {
        if (id == null) {
            return -1L;
        }
        return id;
    }

    public String getFieldsName() {
        return fieldsName;
    }

    public Integer getRecruitCount() {
        return recruitCount;
    }

    public String getTotalAbility() {
        return totalAbility;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public List<UpdateTechStackRequest> getStacks() {
        return stacks;
    }
}
