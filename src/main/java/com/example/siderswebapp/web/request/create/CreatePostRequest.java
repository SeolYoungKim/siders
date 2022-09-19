package com.example.siderswebapp.web.request.create;

import com.example.siderswebapp.domain.RecruitType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static com.example.siderswebapp.domain.RecruitType.PROJECT;
import static com.example.siderswebapp.domain.RecruitType.STUDY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePostRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "모집 분야를 선택해주세요.")
    private String recruitType;

    @NotBlank(message = "연락처를 입력해주세요.")
    private String contact;

    @NotBlank(message = "내용을 입력해주세요.")
    private String recruitIntroduction;

    @NotBlank(message = "예상 소요 기간을 선택해주세요.")
    private String expectedPeriod;

    @Valid
    @NotEmpty(message = "모집 분야는 1개 이상 선택해야 합니다.")
    private List<CreateFieldsRequest> fieldsList;

    @Builder
    public CreatePostRequest(String title, String recruitType, String contact, String recruitIntroduction,
                             String expectedPeriod, List<CreateFieldsRequest> fieldsList) {
        this.title = title;
        this.recruitType = recruitType;
        this.contact = contact;
        this.recruitIntroduction = recruitIntroduction;
        this.expectedPeriod = expectedPeriod;
        this.fieldsList = fieldsList;

    }

    public RecruitType recruitTypeToEnum() {
        return recruitType.equals("스터디") ? STUDY : PROJECT;
    }
}
