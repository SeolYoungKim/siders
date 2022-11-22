package com.example.siderswebapp.post.domain.repository.post;

import static com.example.siderswebapp.post.domain.QFields.fields;
import static com.example.siderswebapp.post.domain.QPost.post;
import static com.example.siderswebapp.post.domain.QTechStack.techStack;

import com.example.siderswebapp.post.application.dto.search.PostSearch;
import com.example.siderswebapp.post.domain.Post;
import com.example.siderswebapp.post.domain.RecruitType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Post> pagingPost(Pageable pageable) {
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

    @Override
    public Page<Post> searchPost(PostSearch postSearch, Pageable pageable) {
        RecruitType recruitType =
                postSearch.getRecruitType().equalsIgnoreCase("TOTAL") ?
                        null : postSearch.recruitTypeToEnum();

        // 조건을 만족하는 post Id만 우선 찾는다. 왜 Id를 찾느냐? 중복을 제거할 수 있는 수단이 Id뿐이다.
        List<Long> postIds = jpaQueryFactory
                .select(post.id).from(post).distinct()
                .innerJoin(post.fieldsList, fields)
                .innerJoin(fields.stacks, techStack)
                .where(searchPost(recruitType, postSearch.getKeyword()))
                .fetch();

        // 여기서는 post를 찾아야 한다. 페이징 해서 보내줘야 하기 때문.
        JPAQuery<Post> find = jpaQueryFactory.selectFrom(post)
                .where(post.isCompleted.eq(false), post.id.in(postIds));

        List<Post> totalList = find.fetch();

        List<Post> postList = find
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
