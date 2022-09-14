package com.example.siderswebapp.service.post;

import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.repository.post.PostRepository;
import com.example.siderswebapp.web.request.CreatePostRequest;
import com.example.siderswebapp.web.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.siderswebapp.domain.RecruitType.PROJECT;
import static com.example.siderswebapp.domain.RecruitType.STUDY;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    // 모집 글 작성
    // TODO: Enum을 직렬화 및 역직렬화해서 사용하려했는데, 이해를 못해서 일단 이렇게 구성함.
    public PostResponse createPost(CreatePostRequest postDto) {
        String recruitType = postDto.getRecruitType();

        Post post = Post.builder()
                .title(postDto.getTitle())
                .recruitType(recruitType.equals("스터디") ? STUDY : PROJECT)
                .contact(postDto.getContact())
                .recruitIntroduction(postDto.getRecruitIntroduction())
                .build();

        //techStackDto랑 Field랑 어떻게 매칭하지..
        //동시에 다른 DTO로 받아오지 않는 이상 동시 처리가 불가능할 것 같다. List -> Map으로 바꿔보자

        postRepository.save(post);

        return new PostResponse(post);
    }
}
