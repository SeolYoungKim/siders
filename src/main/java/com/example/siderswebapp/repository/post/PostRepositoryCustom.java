package com.example.siderswebapp.repository.post;

import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.web.request.post.search.PostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> pagingPost(Pageable pageable);

    Page<Post> searchPost(PostSearch postSearch, Pageable pageable);
}
