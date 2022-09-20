package com.example.siderswebapp.web.request.completion;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IsCompletedDto {

    private Boolean isCompleted;

    public IsCompletedDto(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}