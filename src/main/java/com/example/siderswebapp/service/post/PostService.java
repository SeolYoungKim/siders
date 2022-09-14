package com.example.siderswebapp.service.post;

import com.example.siderswebapp.domain.fields.Fields;
import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.domain.tech_stack.TechStack;
import com.example.siderswebapp.repository.post.PostRepository;
import com.example.siderswebapp.web.request.CreateFieldsRequest;
import com.example.siderswebapp.web.request.CreatePostRequest;
import com.example.siderswebapp.web.request.CreatedTechStackRequest;
import com.example.siderswebapp.web.response.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.siderswebapp.domain.RecruitType.PROJECT;
import static com.example.siderswebapp.domain.RecruitType.STUDY;

@Transactional
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
        List<CreateFieldsRequest> fieldsRequests = postDto.getFieldsRequests();
        for (CreateFieldsRequest fieldsRequest : fieldsRequests) {
            Fields fields = Fields.builder()
                    .post(post)
                    .fieldsName(fieldsRequest.getFieldsName())
                    .recruitCount(fieldsRequest.getRecruitCount())
                    .totalAbility(fieldsRequest.getTotalAbility())
                    .build();

            List<CreatedTechStackRequest> techStackRequests = fieldsRequest.getTechStackRequests();
            for (CreatedTechStackRequest techStackRequest : techStackRequests) {
                TechStack.builder()
                        .fields(fields)
                        .stackName(techStackRequest.getStackName())
                        .build();
            }
        }

        postRepository.save(post);

        return new PostResponse(post);
    }
}
