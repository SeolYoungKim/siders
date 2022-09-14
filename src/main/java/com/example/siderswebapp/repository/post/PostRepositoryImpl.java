package com.example.siderswebapp.repository.post;

import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.web.request.search.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.siderswebapp.domain.post.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getPostList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize() == null? 10 : postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(post.createdDate.desc())
                .fetch();
    }
}
