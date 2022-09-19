package com.example.siderswebapp.repository.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.example.siderswebapp.domain.member.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public String getRefreshTokenById(Long id) {
        return jpaQueryFactory
                .select(member.refreshToken)
                .from(member)
                .where(member.id.eq(id))
                .fetchOne();
    }

    @Override
    public void updateRefreshToken(Long id, String token) {
        jpaQueryFactory.update(member)
                .set(member.refreshToken, token)
                .where(member.id.eq(id));
    }
}
