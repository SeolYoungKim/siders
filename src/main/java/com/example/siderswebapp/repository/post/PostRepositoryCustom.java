package com.example.siderswebapp.repository.post;

import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.web.request.search.PostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> pagingPost(Pageable pageable);

    // TODO: 기본 기능 추가 후, 서치 로직 고민.
    Page<Post> searchByRecruitType(PostSearch postSearch, Pageable pageable);
    Page<Post> searchByFieldsName(PostSearch postSearch, Pageable pageable);
    Page<Post> searchByStackName(PostSearch postSearch, Pageable pageable);
}
