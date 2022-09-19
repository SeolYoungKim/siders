package com.example.siderswebapp.repository.member;

import org.springframework.transaction.annotation.Transactional;

public interface MemberRepositoryCustom {

    String getRefreshTokenById(Long id);

    @Transactional
    void updateRefreshToken(Long id, String token);
}
