package com.example.siderswebapp.web.request.create;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatedTechStackRequest {

    @NotBlank(message = "기술 스택 이름을 입력해주세요.")
    private String stackName;

    public CreatedTechStackRequest(String stackName) {
        this.stackName = stackName;
    }
}
