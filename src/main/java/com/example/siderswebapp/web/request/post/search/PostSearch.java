package com.example.siderswebapp.web.request.post.search;

import com.example.siderswebapp.domain.RecruitType;
import lombok.*;

import static com.example.siderswebapp.domain.RecruitType.PROJECT;
import static com.example.siderswebapp.domain.RecruitType.STUDY;

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
        this.recruitType = recruitType.toUpperCase();
        this.keyword = keyword;
    }

    public RecruitType recruitTypeToEnum() {
        return recruitType.equals("STUDY") ? STUDY : PROJECT;
    }
}
