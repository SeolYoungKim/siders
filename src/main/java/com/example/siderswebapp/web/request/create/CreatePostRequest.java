package com.example.siderswebapp.web.request.create;

import com.example.siderswebapp.domain.RecruitType;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static com.example.siderswebapp.domain.RecruitType.PROJECT;
import static com.example.siderswebapp.domain.RecruitType.STUDY;

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

    private Boolean isCompleted;

    @Valid
    @NotEmpty(message = "모집 분야는 한개 이상 입력되어야 합니다.")
    private List<CreateFieldsRequest> fieldsList;

    @Builder
    public CreatePostRequest(String title, String recruitType, String contact, String recruitIntroduction,
                             Boolean isCompleted, List<CreateFieldsRequest> fieldsList) {
        this.title = title;
        this.recruitType = recruitType;
        this.contact = contact;
        this.recruitIntroduction = recruitIntroduction;
        this.isCompleted = isCompleted;
        this.fieldsList = fieldsList;
    }

    public RecruitType recruitTypeToEnum() {
        return recruitType.equals("스터디") ? STUDY : PROJECT;
    }
}
