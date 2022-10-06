package com.example.siderswebapp.repository.post;

import com.example.siderswebapp.domain.RecruitType;
import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.web.request.post.search.PostSearch;
import com.querydsl.core.types.dsl.BooleanExpression;
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

    //TODO: 이렇게 구현하는게 맞는지 모르겠다.. 좀 더 고민을 해보자..
    @Override
    public Page<Post> searchPost(PostSearch postSearch, Pageable pageable) {
        RecruitType recruitType =
                postSearch.getRecruitType().equalsIgnoreCase("TOTAL") ?
                        null : postSearch.recruitTypeToEnum();

        JPAQuery<Post> join = jpaQueryFactory
                .selectFrom(post).distinct()
                .join(post.fieldsList, fields)
                .join(fields.stacks, techStack);

        JPAQuery<Post> find;

        if (recruitType == null) {
            find = join
                    .where(keywordCheck(postSearch.getKeyword()));
        } else {
            find = join
                    .where(recruitTypeCheck(recruitType).and(keywordCheck(postSearch.getKeyword())));
        }

        List<Post> totalList = find.fetch();

        List<Post> postList = find
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .orderBy(post.createdDate.desc())
                .fetch();

        return new PageImpl<>(postList, pageable, totalList.size());
    }

    private BooleanExpression recruitTypeCheck(RecruitType recruitType) {
        return post.recruitType.eq(recruitType);
    }

    // 같은 글이 두개가 조회가 되는 현상이 있다. 아무래도 여기가 문제인가?
    private BooleanExpression keywordCheck(String keyword) {
        BooleanExpression fieldsAndTechStackCheck =
                fields.fieldsName.contains(keyword).or(techStack.stackName.contains(keyword));

        BooleanExpression postTitleAndContentsCheck =
                post.recruitIntroduction.contains(keyword).or(post.title.contains(keyword));

        return postTitleAndContentsCheck.or(fieldsAndTechStackCheck);
    }
}
