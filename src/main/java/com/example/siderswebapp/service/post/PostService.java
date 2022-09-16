package com.example.siderswebapp.service.post;

import com.example.siderswebapp.domain.fields.Fields;
import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.domain.tech_stack.TechStack;
import com.example.siderswebapp.repository.fields.FieldsRepository;
import com.example.siderswebapp.repository.post.PostRepository;
import com.example.siderswebapp.repository.tech_stack.TechStackRepository;
import com.example.siderswebapp.web.request.create.CreateFieldsRequest;
import com.example.siderswebapp.web.request.create.CreatePostRequest;
import com.example.siderswebapp.web.request.create.CreatedTechStackRequest;
import com.example.siderswebapp.web.request.update.UpdateFieldsRequest;
import com.example.siderswebapp.web.request.update.UpdatePostRequest;
import com.example.siderswebapp.web.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final FieldsRepository fieldsRepository;
    private final TechStackRepository techStackRepository;

    // 모집 글 작성
    // TODO: Enum을 직렬화 및 역직렬화해서 사용하려했는데, 이해를 못해서 일단 이렇게 구성함. 적용 하더라도, 공부 후 적용하자!
    public PostResponse createPost(CreatePostRequest postDto) {

        Post post = Post.builder()
                .title(postDto.getTitle())
                .recruitType(postDto.recruitTypeToEnum())
                .contact(postDto.getContact())
                .recruitIntroduction(postDto.getRecruitIntroduction())
                .build();

        List<CreateFieldsRequest> fieldsRequests = postDto.getFieldsList();
        for (CreateFieldsRequest fieldsRequest : fieldsRequests) {
            Fields fields = Fields.builder()
                    .post(post)
                    .fieldsName(fieldsRequest.getFieldsName())
                    .recruitCount(fieldsRequest.getRecruitCount())
                    .totalAbility(fieldsRequest.getTotalAbility())
                    .build();

            List<CreatedTechStackRequest> techStackRequests = fieldsRequest.getStacks();
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

    @Transactional(readOnly = true)
    public PostResponse readPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("없는 아이디"));
        return new PostResponse(post);  // TODO: 아예 DTO로 조회해오는 로직이 있으면 한줄 더 감소할 것 같다.(영한님 강의 참고)
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getPostList(Pageable pageable) {
        return postRepository.pagingPost(pageable).map(PostResponse::new);
    }

    public PostResponse updatePost(Long id, UpdatePostRequest postDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("없는 아이디"));

        // 이거는 있는것만 수정하기 때문에 추가 로직이 필요 없다.
        post.updatePost(postDto);

        // TODO: field를 유저가 삭제할 경우 어떻게 처리할지 생각
        List<UpdateFieldsRequest> fieldsDtoList = postDto.getFieldsList();
        for (UpdateFieldsRequest fieldDto : fieldsDtoList) {

            Fields fields = fieldsRepository.findById(fieldDto.getId() != null ? fieldDto.getId() : -1L)  // id가 없는 경우, -1L로 찾으므로써 null 유도.
                    .orElseGet(() -> Fields.builder()
                            .post(post)
                            .fieldsName(fieldDto.getFieldsName())
                            .recruitCount(fieldDto.getRecruitCount())
                            .totalAbility(fieldDto.getTotalAbility())
                            .build());

            // 찾았는데 Dto의 isDelete가 true야 그럼 필드 삭제해버리고 다음필드 탐색 ㄱ
            if (fieldDto.getIsDelete()) {
                post.getFieldsList().remove(fields);
                continue;
            }

            fields.updateFields(fieldDto);

            // 기술 스택을 수정 데이터로 새로 갈아끼우기 위해 Fields의 List<TechStack>을 비운다
            fields.getStacks().clear();

            // 비운 List<TechStack>을 dto가 가져온 값으로 싹 갈아끼운다.
            fieldDto.getStacks()
                    .forEach(stackDto ->
                            TechStack.builder()
                                    .stackName(stackDto.getStackName())
                                    .fields(fields)
                                    .build());

        }

        postRepository.save(post);  // 새로운 필드나 스택이 추가될 수 있기 때문에 저장

        return new PostResponse(post);
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("없는 아이디"));

        postRepository.delete(post);
    }
}







