package com.example.siderswebapp.web.request.update;

import com.example.siderswebapp.domain.RecruitType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.example.siderswebapp.domain.RecruitType.PROJECT;
import static com.example.siderswebapp.domain.RecruitType.STUDY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdatePostRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String recruitType;

    @NotBlank
    private String contact;

    @NotBlank
    private String recruitIntroduction;

    @NotBlank
    private List<UpdateFieldsRequest> fieldsList;

    @Builder
    public UpdatePostRequest(String title, String recruitType, String contact, String recruitIntroduction, List<UpdateFieldsRequest> fieldsList) {
        this.title = title;
        this.recruitType = recruitType;
        this.contact = contact;
        this.recruitIntroduction = recruitIntroduction;
        this.fieldsList = fieldsList;
    }

    public RecruitType recruitTypeToEnum() {
        return recruitType.equals("스터디") ? STUDY : PROJECT;
    }

}
