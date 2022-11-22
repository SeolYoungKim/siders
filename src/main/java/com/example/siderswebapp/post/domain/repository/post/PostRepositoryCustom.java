package com.example.siderswebapp.post.domain.repository.post;

import com.example.siderswebapp.post.domain.Post;
import com.example.siderswebapp.post.application.dto.search.PostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> pagingPost(Pageable pageable);

    Page<Post> searchPost(PostSearch postSearch, Pageable pageable);
}
