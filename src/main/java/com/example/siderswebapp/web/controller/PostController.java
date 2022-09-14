package com.example.siderswebapp.web.controller;

import com.example.siderswebapp.service.post.PostService;
import com.example.siderswebapp.web.response.PostResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final ObjectMapper objectMapper;

    @GetMapping("/")
    public String home() {
        return "hi";
    }

    @GetMapping("/recruitment")
    public String recruitmentView() {
        //TODO: FieldsType이랑, RecruitType을 내려준다. (Front에서 사용해야 됨)
        return "";
    }

    @PostMapping("/recruitment")
    public PostResponse recruitmentWrite(@RequestBody String json) {
        log.info("이거 되긴 할까??????? >>>>>> " + json);
        return null;
    }
}
