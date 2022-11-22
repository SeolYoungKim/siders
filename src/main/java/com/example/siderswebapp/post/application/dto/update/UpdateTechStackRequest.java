package com.example.siderswebapp.post.application.dto.update;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateTechStackRequest {

    @NotBlank(message = "기술 스택 이름을 입력해주세요.")
    private String stackName;

    @Builder
    public UpdateTechStackRequest(String stackName) {
        this.stackName = stackName;
    }
}
