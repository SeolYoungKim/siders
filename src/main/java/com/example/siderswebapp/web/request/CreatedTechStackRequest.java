package com.example.siderswebapp.web.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class CreatedTechStackRequest {

    @NotBlank
    private final String stackName;

    public CreatedTechStackRequest(String stackName) {
        this.stackName = stackName;
    }
}
