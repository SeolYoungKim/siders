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

    //TODO: 여기서 왜 리디렉션이 안가지...
    // https://stackoverflow.com/questions/34595605/how-to-manage-exceptions-thrown-in-filters-in-spring
    // https://codingdog.tistory.com/entry/spring-security-filter-exception-%EC%9D%84-custom-%ED%95%98%EA%B2%8C-%EC%B2%98%EB%A6%AC%ED%95%B4-%EB%B4%85%EC%8B%9C%EB%8B%A4
    // 직접 여기서 예외를 처리해서 보내주거나, 이거 전에 필터를 하나 두고, 거기서 try-catch로 예외를 처리해준다.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwt = jwtProvider.resolveToken(request);

        if (StringUtils.hasText(jwt)) {  // 토큰이 있으면
            if (jwtProvider.validateToken(jwt)) {  // 토큰을 검증하고 (올바르지 않은 경우 여기서 예외 발생)
                // 토큰이 유효하면
                Authentication authentication = jwtProvider.authenticate(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else {
            log.info("미인증 사용자 입니다.");
        }

        // 토큰이 없으면 SecurityContext에 인증 객체 넣지 않고 그냥 필터 진행
        filterChain.doFilter(request, response);
    }

    // 회원가입 시에는 해당 필터 미적용
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().equals("/api/signup");
    }
}
