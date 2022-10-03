package com.example.siderswebapp.service.post;

import com.example.siderswebapp.domain.fields.Fields;
import com.example.siderswebapp.domain.member.Member;
import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.domain.tech_stack.TechStack;
import com.example.siderswebapp.exception.PostNotExistException;
import com.example.siderswebapp.repository.fields.FieldsRepository;
import com.example.siderswebapp.repository.member.MemberRepository;
import com.example.siderswebapp.repository.post.PostRepository;
import com.example.siderswebapp.web.request.post.completion.IsCompletedDto;
import com.example.siderswebapp.web.request.post.create.CreateFieldsRequest;
import com.example.siderswebapp.web.request.post.create.CreatePostRequest;
import com.example.siderswebapp.web.request.post.update.UpdateFieldsRequest;
import com.example.siderswebapp.web.request.post.update.UpdatePostRequest;
import com.example.siderswebapp.web.response.post.create.PostIdDto;
import com.example.siderswebapp.web.response.post.read.ReadPostResponse;
import com.example.siderswebapp.web.response.post.read.paging.PagingPostsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
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
    private final MemberRepository memberRepository;

    // 모집 글 작성
    // TODO: Enum을 직렬화 및 역직렬화해서 사용하려했는데, 이해를 못해서 일단 이렇게 구성함. 적용 하더라도, 공부 후 적용하자!
    public PostIdDto createPost(CreatePostRequest postDto, Authentication authentication) {

        // 멤버를 찾는다.
        String authId = getAuthId(authentication);
        Member member = memberRepository.findByAuthId(authId)
                .orElseThrow(IllegalAccessError::new);  // 나중에 커스텀 예외처리.

        Post post = Post.builder()
                .title(postDto.getTitle())
                .recruitType(postDto.recruitTypeToEnum())
                .contact(postDto.getContact())
                .recruitIntroduction(postDto.getRecruitIntroduction())
                .expectedPeriod(postDto.getExpectedPeriod())
                .member(member)  // 멤버를 넣어준다.
                .isCompleted(false)
                .build();

        List<CreateFieldsRequest> fieldsRequests = postDto.getFieldsList();
        for (CreateFieldsRequest fieldsRequest : fieldsRequests) {
            Fields fields = Fields.builder()
                    .post(post)
                    .fieldsName(fieldsRequest.getFieldsName())
                    .recruitCount(fieldsRequest.getRecruitCount())
                    .totalAbility(fieldsRequest.totalAbilityToEnum())
                    .build();

            fieldsRequest.getStacks()
                    .forEach(techStackDto -> TechStack.builder()
                            .fields(fields)
                            .stackName(techStackDto.getStackName())
                            .build());
        }

        Post savedPost = postRepository.save(post);

        return new PostIdDto(savedPost.getId());
    }

    @Transactional(readOnly = true)
    public ReadPostResponse readPost(Long id, Authentication authentication) {

        // TODO: 아예 DTO로 조회해오는 로직이 있으면 한줄 더 감소할 것 같다.(영한님 강의 참고)
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotExistException::new);

        // 조회 화면에서는 인증된 유저가 넘어오지 않을 수도 있다. NPE 방지를 위해 아래와 같이 구성.
        String authId = getAuthId(authentication);

        return new ReadPostResponse(post, post.isWriter(authId));
    }

    @Transactional(readOnly = true)
    public Page<PagingPostsResponse> getPostList(Pageable pageable) {
        return postRepository.pagingPost(pageable).map(PagingPostsResponse::new);
    }

    public PostIdDto updatePost(Long id, UpdatePostRequest postDto,
                                Authentication authentication) throws IllegalAccessException {

        String authId = getAuthId(authentication);

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotExistException::new);

        if (!post.isWriter(authId))
            throw new IllegalAccessException();

        // 이거는 있는것만 수정하기 때문에 추가 로직이 필요 없다.
        post.updatePost(postDto);

        List<UpdateFieldsRequest> fieldsDtoList = postDto.getFieldsList();
        for (UpdateFieldsRequest fieldDto : fieldsDtoList) {

            Fields fields = fieldsRepository.findById(fieldDto.getId() != null ? fieldDto.getId() : -1L)  // id가 없는 경우, -1L로 찾으므로써 null 유도.
                    .orElseGet(() -> Fields.builder()
                            .post(post)
                            .fieldsName(fieldDto.getFieldsName())
                            .recruitCount(fieldDto.getRecruitCount())
                            .totalAbility(fieldDto.totalAbilityToEnum())
                            .build());

            // 찾았는데 Dto의 isDelete가 true면 필드 삭제해버리고 다음필드 탐색 ㄱ
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

        return new PostIdDto(id);
    }


    public PostIdDto changeCompletion(Long id, IsCompletedDto isCompletedDto,
                                         Authentication authentication) throws IllegalAccessException {

        String authId = getAuthId(authentication);

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotExistException::new);

        if (!post.isWriter(authId))
            throw new IllegalAccessException();

        post.changeCompletion(isCompletedDto.getIsCompleted());

        return new PostIdDto(post.getId());
    }

    public void deletePost(Long id, Authentication authentication) throws IllegalAccessException {

        String authId = getAuthId(authentication);

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotExistException::new);

        if (!post.isWriter(authId))
            throw new IllegalAccessException();

        // member의 orphanremoval 옵션 때문에, member의 List에서 제거해줘야 post가 삭제 됨
        Member member = memberRepository.findByAuthId(authId)
                .orElseThrow(IllegalAccessException::new);

        member.getPostList().remove(post);
        log.info("Member's PostList size = {}", member.getPostList().size());

        postRepository.delete(post);
    }

    // Authentication이 null일 경우 "" 반환
    private String getAuthId(Authentication authentication) {
        return authentication != null ? authentication.getName() : "";
    }
}







