package com.example.siderswebapp.web.request.update;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateTechStackRequest {

    private Long id;

    @NotBlank
    private String stackName;

    @Builder
    public UpdateTechStackRequest(Long id, String stackName) {
        this.id = id;
        this.stackName = stackName;
    }
}
