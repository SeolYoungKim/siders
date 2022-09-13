package com.example.siderswebapp.service.post;

import com.example.siderswebapp.domain.fileds.Fields;
import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.repository.post.PostRepository;
import com.example.siderswebapp.web.request.CreatePostRequest;
import com.example.siderswebapp.web.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

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

        Post recruitPost = Post.builder()
                .title(postDto.getTitle())
                .recruitType(recruitType.equals("스터디") ? STUDY : PROJECT)
                .memberCount(postDto.getMemberCount())
                .contact(postDto.getContact())
                .recruitContent(postDto.getRecruitContent())
                .build();

        // field 정보도 받아온 후, 매핑시켜주어야 함
        Fields fields = Fields.builder()
                .post(recruitPost)
                .fieldsName(postDto.getFieldsName())
                .build();

        recruitPost.addField(fields);

        Map<String, String> stackNamesAndAbilities = postDto.getStackNamesAndAbilities();
        Set<String> stackNames = stackNamesAndAbilities.keySet();

        return new PostResponse(recruitPost);
    }
}
