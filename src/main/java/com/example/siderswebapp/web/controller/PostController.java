package com.example.siderswebapp.web.controller;

import com.example.siderswebapp.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/")
    public String home() {
        return "hi";
    }

    @GetMapping("/recruitment")
    public String recruitmentView() {
        return "";
    }

    @PostMapping("/recruitment")
    public String recruitmentWrite() {
        return "";
    }
}
