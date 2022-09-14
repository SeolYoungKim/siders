package com.example.siderswebapp.web.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatedTechStackRequest {

    @NotBlank
    private String stackName;

    public CreatedTechStackRequest(String stackName) {
        this.stackName = stackName;
    }
}
