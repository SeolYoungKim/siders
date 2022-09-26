package com.example.siderswebapp.web.controller;

import com.example.siderswebapp.service.post.PostService;
import com.example.siderswebapp.web.request.completion.IsCompletedDto;
import com.example.siderswebapp.web.request.create.CreatePostRequest;
import com.example.siderswebapp.web.request.update.UpdatePostRequest;
import com.example.siderswebapp.web.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/recruitment")
    public String recruitmentView() {
        return "";
    }

    // 글 등록
    @PostMapping("/recruitment")
    public PostResponse recruitmentWrite(@Valid @RequestBody CreatePostRequest postDto) {
        return postService.createPost(postDto);
    }

    // 단건 조회 TODO: 단건조회 시 유저 정보를 내려줌으로써 Post 객체에 저장된 유저 정보와 비교하여 버튼이 노출되도록 수정
    @GetMapping("/post/{id}")
    public PostResponse readPost(@PathVariable Long id) {
        return postService.readPost(id);
    }

    // 여러 건 조회 + 페이징 (쿼리 파라미터 사용)
    // PostSearch는 나중에 검색용으로 사용하자. (DTO) @ModelAttribute PostSearch postSearch
    @GetMapping("/")
    public Page<PostResponse> paging(Pageable pageable) {
        return postService.getPostList(pageable);
    }

    // 글 수정
    @PutMapping("/post/{id}")
    public PostResponse updatePost(@PathVariable Long id,
                                   @Valid @RequestBody UpdatePostRequest postDto) {
        return postService.updatePost(id, postDto);
    }

    // 모집 완료 여부 변경
    @PatchMapping("/post/{id}")
    public PostResponse completedPost(@PathVariable Long id,
                                      @RequestBody IsCompletedDto isCompletedDto) {
        return postService.changeCompletion(id, isCompletedDto);
    }

    // 글 삭제
    @DeleteMapping("/post/{id}")
    public void deletePost(@PathVariable Long id) {
        postService.deletePost(id);
    }


}








