package com.example.siderswebapp.auth.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = jwtProvider.resolveToken(request);

        if (StringUtils.hasText(jwt)) {  // 토큰이 있으면
            if (jwtProvider.validateToken(jwt)) {  // 토큰을 검증하고 (올바르지 않은 경우 여기서 예외 발생)
                Authentication authentication = jwtProvider.authenticate(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            log.info("미인증 사용자 입니다.");
        }

        // 토큰이 없으면 SecurityContext에 인증 객체 넣지 않고 그냥 필터 진행
        filterChain.doFilter(request, response);
    }

}
