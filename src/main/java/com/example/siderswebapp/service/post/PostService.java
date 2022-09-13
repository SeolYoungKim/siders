package com.example.siderswebapp.service.post;

import com.example.siderswebapp.repository.post.PostRepository;
import com.example.siderswebapp.web.request.CreatePostRequest;
import com.example.siderswebapp.web.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    // 모집 글 작성
    // TODO: Enum을 직렬화 및 역직렬화해서 사용하려했는데, 이해를 못해서 일단 이렇게 구성함.
    public PostResponse createPost(CreatePostRequest postDto) {


        return null;
    }
}
