package com.example.siderswebapp.web.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePostRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String recruitType;

    @NotBlank
    private String contact;

    @NotBlank
    private String recruitIntroduction;

    @NotBlank
    private List<CreateFieldsRequest> fieldsRequests;

    @Builder
    public CreatePostRequest(String title, String recruitType, String contact, String recruitIntroduction, List<CreateFieldsRequest> fieldsRequests) {
        this.title = title;
        this.recruitType = recruitType;
        this.contact = contact;
        this.recruitIntroduction = recruitIntroduction;
        this.fieldsRequests = fieldsRequests;
    }
}
