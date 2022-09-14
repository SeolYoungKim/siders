package com.example.siderswebapp.web.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter @Setter
public class CreatePostRequest {
    @NotBlank
    private final String title;

    @NotBlank
    private final String recruitType;

    @NotBlank
    private final String contact;

    @NotBlank
    private final String recruitIntroduction;

    @NotBlank
    private final List<CreateFieldsRequest> fieldsRequests;

    @Builder
    public CreatePostRequest(String title, String recruitType, String contact, String recruitIntroduction, List<CreateFieldsRequest> fieldsRequests) {
        this.title = title;
        this.recruitType = recruitType;
        this.contact = contact;
        this.recruitIntroduction = recruitIntroduction;
        this.fieldsRequests = fieldsRequests;
    }
}
