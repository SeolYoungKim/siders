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

        // 토큰이 유효하지 않으면, 재 로그인 요청을 보낼까?
        if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
            Authentication authentication = jwtProvider.authenticate(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else { // 토큰이 없거나 유효하지 않은 경우 -> 만료 페이지로 리디렉션
            log.info("토큰이 없거나 만료되었습니다. 다시 로그인 해주세요.");
        }

        filterChain.doFilter(request, response);
    }

    // 회원가입 시에는 해당 필터 미적용
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().equals("/api/signup");
    }
}
