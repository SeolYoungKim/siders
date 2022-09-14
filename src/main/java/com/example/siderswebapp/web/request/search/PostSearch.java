package com.example.siderswebapp.web.request.search;

import lombok.Builder;
import lombok.Getter;

import static java.lang.Math.*;

@Getter
public class PostSearch {
    private Integer page;  // 1
    private Integer size;  // 10

    @Builder
    public PostSearch(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    public Integer getOffset() {
        page = page == null? 1 : page;
        size = size == null? 10 : size;

        return (max(page, 1) - 1) * min(size, 100);
    }

}
