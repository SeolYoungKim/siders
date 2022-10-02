package com.example.siderswebapp.web.controller;

import com.example.siderswebapp.service.post.PostService;
import com.example.siderswebapp.web.request.post.completion.IsCompletedDto;
import com.example.siderswebapp.web.request.post.create.CreatePostRequest;
import com.example.siderswebapp.web.request.post.update.UpdatePostRequest;
import com.example.siderswebapp.web.response.post.create.PostIdDto;
import com.example.siderswebapp.web.response.post.read.ReadPostResponse;
import com.example.siderswebapp.web.response.post.read.paging.PagingPostsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/recruitment")
    public String recruitmentView() {
        return "";
    }

    // 글 등록
    @PostMapping("/recruitment")
    public PostIdDto recruitmentWrite(@Valid @RequestBody CreatePostRequest postDto,
                                      Authentication authentication) {

        return postService.createPost(postDto, authentication);
    }

    // 단건 조회
    @GetMapping("/post/{id}")
    public ReadPostResponse readPost(@PathVariable Long id, Authentication authentication) {
        return postService.readPost(id, authentication);
    }

    // 여러 건 조회 + 페이징 (쿼리 파라미터 사용)
    // PostSearch는 나중에 검색용으로 사용하자. (DTO) @ModelAttribute PostSearch postSearch
    @GetMapping("/posts")
    public Page<PagingPostsResponse> paging(Pageable pageable) {
        return postService.getPostList(pageable);
    }

    // 글 수정
    @PutMapping("/post/{id}")
    public PostIdDto updatePost(@PathVariable Long id,
                                   @Valid @RequestBody UpdatePostRequest postDto,
                                   Authentication authentication) throws IllegalAccessException {
        return postService.updatePost(id, postDto, authentication);
    }

    // 모집 완료 여부 변경
    @PatchMapping("/post/{id}")
    public PostIdDto completedPost(@PathVariable Long id,
                                   @RequestBody IsCompletedDto isCompletedDto,
                                   Authentication authentication) throws IllegalAccessException {
        return postService.changeCompletion(id, isCompletedDto, authentication);
    }

    // 글 삭제
    @DeleteMapping("/post/{id}")
    public void deletePost(@PathVariable Long id,
                           Authentication authentication) throws IllegalAccessException {
        postService.deletePost(id, authentication);
    }


}








