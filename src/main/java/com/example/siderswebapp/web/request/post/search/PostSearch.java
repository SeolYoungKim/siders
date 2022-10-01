package com.example.siderswebapp.web.request.post.search;

import com.example.siderswebapp.domain.RecruitType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.example.siderswebapp.domain.RecruitType.*;

/**
 * 검색용 DTO로 사용하자.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSearch {
    private String recruitType;
    private String fieldsName;
    private String stackName;

    @Builder
    public PostSearch(String recruitType, String fieldsName, String stackName) {
        this.recruitType = recruitType;
        this.fieldsName = fieldsName;
        this.stackName = stackName;
    }

    public RecruitType recruitTypeToEnum() {
        return recruitType.equals("스터디") ? STUDY : PROJECT;
    }
}
