package com.example.siderswebapp.post.application;

import com.example.siderswebapp.exception.domain.MemberNotFoundException;
import com.example.siderswebapp.exception.domain.PostNotFoundException;
import com.example.siderswebapp.member.domain.AuthId;
import com.example.siderswebapp.member.domain.Member;
import com.example.siderswebapp.member.domain.repository.MemberRepository;
import com.example.siderswebapp.post.application.dto.completion.IsCompletedDto;
import com.example.siderswebapp.post.application.dto.create.CreateFieldsRequest;
import com.example.siderswebapp.post.application.dto.create.CreatePostRequest;
import com.example.siderswebapp.post.application.dto.search.PostSearch;
import com.example.siderswebapp.post.application.dto.update.UpdateFieldsRequest;
import com.example.siderswebapp.post.application.dto.update.UpdatePostRequest;
import com.example.siderswebapp.post.domain.Fields;
import com.example.siderswebapp.post.domain.FieldsFactory;
import com.example.siderswebapp.post.domain.Post;
import com.example.siderswebapp.post.domain.PostFactory;
import com.example.siderswebapp.post.domain.TechStackFactory;
import com.example.siderswebapp.post.domain.repository.fields.FieldsRepository;
import com.example.siderswebapp.post.domain.repository.post.PostRepository;
import com.example.siderswebapp.post.presentation.dto.create.PostIdDto;
import com.example.siderswebapp.post.presentation.dto.read.ReadPostResponse;
import com.example.siderswebapp.post.presentation.dto.read.paging.PagingPostsResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final FieldsRepository fieldsRepository;
    private final MemberRepository memberRepository;

    // 모집 글 작성
    public PostIdDto createPost(CreatePostRequest postDto,
            UsernamePasswordAuthenticationToken authentication) {
        String authId = getAuthId(authentication);
        Member member = memberRepository.findByAuthId(new AuthId(authId))
                .orElseThrow(MemberNotFoundException::new);

        Post post = PostFactory.newInstance(postDto, member);

        List<CreateFieldsRequest> fieldsRequests = postDto.getFieldsList();
        List<Fields> fieldsList = FieldsFactory.fieldsList(fieldsRequests, post);
        TechStackFactory.saveNewTechStackToFields(fieldsRequests, fieldsList);

        postRepository.save(post);

        return new PostIdDto(post.getId());
    }

    @Transactional(readOnly = true)
    public ReadPostResponse readPost(Long id, Authentication authentication) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        String authId = getAuthId(authentication);
        return new ReadPostResponse(post, post.writtenBy(authId));
    }

    @Transactional(readOnly = true)
    public Page<PagingPostsResponse> getPostList(Pageable pageable) {
        return postRepository.pagingPost(pageable).map(PagingPostsResponse::new);
    }

    @Transactional(readOnly = true)
    public Page<PagingPostsResponse> searchPost(PostSearch postSearch, Pageable pageable) {
        return postRepository.searchPost(postSearch, pageable).map(PagingPostsResponse::new);
    }

    public PostIdDto updatePost(Long id, UpdatePostRequest postDto,
            Authentication authentication) {
        String authId = getAuthId(authentication);

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        post.updatePost(postDto, authId);

        List<UpdateFieldsRequest> fieldsDtoList = postDto.getFieldsList();
        for (UpdateFieldsRequest fieldDto : fieldsDtoList) {
            Fields fields = fieldsRepository.findById(fieldDto.getId())
                    .orElseGet(() -> FieldsFactory.newInstance(post, fieldDto));

            if (fieldDto.getIsDelete()) {
                post.removeFields(fields);
                continue;
            }

            fields.updateFields(fieldDto);
            fields.clearStacks();
            fieldDto.getStacks()
                    .forEach(stackDto -> TechStackFactory.newInstance(fields, stackDto));
        }

        postRepository.save(post);

        return new PostIdDto(id);
    }


    public PostIdDto changeCompletion(Long id, IsCompletedDto isCompletedDto,
            UsernamePasswordAuthenticationToken authentication) {
        String authId = getAuthId(authentication);

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        post.validateIsWriter(authId);
        post.changeCompletion(isCompletedDto.getIsCompleted());

        return new PostIdDto(post.getId());
    }

    public void deletePost(Long id, UsernamePasswordAuthenticationToken authentication) {
        String authId = getAuthId(authentication);

        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFoundException::new);

        post.validateIsWriter(authId);

        Member member = memberRepository.findByAuthId(new AuthId(authId))
                .orElseThrow(MemberNotFoundException::new);

        member.removePost(post);
    }

    // Authentication이 null일 경우 "" 반환
    private String getAuthId(Authentication authentication) {
        if (authentication != null) {
            return authentication.getName();
        }
        return "";
    }
}







