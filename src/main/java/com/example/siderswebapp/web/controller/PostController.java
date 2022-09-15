package com.example.siderswebapp.web.controller;

import com.example.siderswebapp.service.post.PostService;
import com.example.siderswebapp.web.request.create.CreatePostRequest;
import com.example.siderswebapp.web.request.search.PostSearch;
import com.example.siderswebapp.web.request.update.UpdatePostRequest;
import com.example.siderswebapp.web.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/recruitment")
    public String recruitmentView() {
        //TODO: FieldsType이랑, RecruitType을 내려준다. (Front에서 사용해야 됨)
        // 해당 컨트롤러는 글 작성 페이지를 나타내기 위한 정보를 전달
        return "";
    }

    // 글 등록
    @PostMapping("/recruitment")
    public PostResponse recruitmentWrite(@RequestBody CreatePostRequest postDto) {
        return postService.createPost(postDto);
    }

    // 단건 조회
    @GetMapping("/post/{id}")
    public PostResponse readPost(@PathVariable Long id) {
        return postService.readPost(id);
    }

    // 여러 건 조회 + 페이징 (쿼리 파라미터 사용)
    // PostSearch는 나중에 검색용으로 사용하자. (DTO)
    @GetMapping("/")
    public Page<PostResponse> test(@ModelAttribute PostSearch postSearch, Pageable pageable) {
        return postService.getPostList(pageable);
    }

    // 글 수정
    @PatchMapping("/post/{id}")
    public PostResponse updatePost(@PathVariable Long id, @RequestBody UpdatePostRequest postDto) {
        return postService.updatePost(id, postDto);
    }
}








