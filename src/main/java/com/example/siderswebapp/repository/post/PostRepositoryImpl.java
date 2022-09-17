package com.example.siderswebapp.repository.post;

import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.web.request.search.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.siderswebapp.domain.post.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    //postSearch에는 가변 값이 담겨있다. (검색 정보.. 등)
    //pageable에는 기본적인 페이징을 위한 값이 담겨있다.
    //혼용해서 사용해보자.

    @Override
    public Page<Post> pagingPost(Pageable pageable) {

        // 모집 완료 글이 아닌 경우에만 노출
        List<Post> totalList = jpaQueryFactory.selectFrom(post)
                .where(post.isCompleted.eq(false))
                .fetch();

        List<Post> postList = jpaQueryFactory.selectFrom(post)
                .where(post.isCompleted.eq(false))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(post.createdDate.desc())
                .fetch();

        return new PageImpl<>(postList, pageable, totalList.size());
    }

    @Override
    public Page<Post> searchByRecruitType(PostSearch postSearch, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Post> searchByFieldsName(PostSearch postSearch, Pageable pageable) {
        return null;
    }

    @Override
    public Page<Post> searchByStackName(PostSearch postSearch, Pageable pageable) {
        return null;
    }

}
