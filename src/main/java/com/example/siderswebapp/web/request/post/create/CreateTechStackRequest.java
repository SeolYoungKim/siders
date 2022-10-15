package com.example.siderswebapp.web.request.post.create;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateTechStackRequest {

    @NotBlank(message = "기술 스택 이름을 입력해주세요.")
    private String stackName;

    public CreateTechStackRequest(String stackName) {
        this.stackName = stackName;
    }
}
