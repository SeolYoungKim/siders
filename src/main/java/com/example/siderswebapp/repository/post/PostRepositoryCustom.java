package com.example.siderswebapp.repository.post;

import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.web.request.search.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getPostList(PostSearch postSearch);
}
