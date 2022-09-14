package com.example.siderswebapp.web.controller;

import com.example.siderswebapp.service.post.PostService;
import com.example.siderswebapp.web.request.create.CreatePostRequest;
import com.example.siderswebapp.web.request.search.PostSearch;
import com.example.siderswebapp.web.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/")
    public List<PostResponse> home(@ModelAttribute PostSearch postSearch) {
        return postService.getPostList(postSearch);
    }
}
