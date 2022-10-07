package com.example.siderswebapp.repository.post;

import com.example.siderswebapp.domain.RecruitType;
import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.web.request.post.search.PostSearch;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.siderswebapp.domain.fields.QFields.fields;
import static com.example.siderswebapp.domain.post.QPost.post;
import static com.example.siderswebapp.domain.tech_stack.QTechStack.techStack;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    //postSearch에는 가변 값이 담겨있다. (검색 정보.. 등)
    //pageable에는 기본적인 페이징을 위한 값이 담겨있다.
    //혼용해서 사용해보자.

    @Override
    public Page<Post> pagingPost(Pageable pageable) {

        // 모집 완료 글이 아닌 경우에만 노출
        JPAQuery<Post> find = jpaQueryFactory.selectFrom(post)
                .where(post.isCompleted.eq(false));

        List<Post> totalList = find.fetch();

        List<Post> postList = find
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(post.createdDate.desc())
                .fetch();

        return new PageImpl<>(postList, pageable, totalList.size());
    }

    //TODO: N+1 문제가 해결이 안된다. -> 방법을 지속적으로 강구해보자..
    @Override
    public Page<Post> searchPost(PostSearch postSearch, Pageable pageable) {
        RecruitType recruitType =
                postSearch.getRecruitType().equalsIgnoreCase("TOTAL") ?
                        null : postSearch.recruitTypeToEnum();

        JPAQuery<Post> join = jpaQueryFactory
                .selectFrom(post).distinct()
                .innerJoin(post.fieldsList, fields).fetchJoin()
                .innerJoin(fields.stacks, techStack)
                .where(searchPost(recruitType, postSearch.getKeyword()));

        List<Post> totalList = join.fetch();

        List<Post> postList = join
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(post.createdDate.desc())
                .fetch();

        return new PageImpl<>(postList, pageable, totalList.size());
    }

    private BooleanBuilder searchPost(RecruitType recruitType, String keyword) {

        BooleanBuilder builder = new BooleanBuilder();

        builder.or(fields.fieldsName.contains(keyword));
        builder.or(techStack.stackName.contains(keyword));
        builder.or(post.recruitIntroduction.contains(keyword));
        builder.or(post.title.contains(keyword));

        if (recruitType != null) {
            builder.and(post.recruitType.eq(recruitType));
        }


        return builder;
    }
}
