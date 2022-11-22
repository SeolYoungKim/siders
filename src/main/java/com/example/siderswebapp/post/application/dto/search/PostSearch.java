package com.example.siderswebapp.post.application.dto.search;

import com.example.siderswebapp.post.domain.RecruitType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 검색용 DTO로 사용하자.
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSearch {
    private String recruitType;
    private String keyword;

    @Builder
    public PostSearch(String recruitType, String keyword) {
        this.recruitType = recruitType;
        this.keyword = keyword;
    }

    public RecruitType recruitTypeToEnum() {
        return RecruitType.valueOf(recruitType.toUpperCase());
    }
}
