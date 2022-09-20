package com.example.siderswebapp.config.oauth.jwt;

import com.example.siderswebapp.config.oauth.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * API 서버에 접근할 때, Auth 헤더에 발급받은 토큰을 함께 보내면,
 * 토큰 값에서 유저 정보를 가져와 회원가입이 되었는지 검증 가능
 * 토큰 존재 여부
 * 토큰의 유효성 및 유효기간 검사
 */

@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {

    private final TokenService tokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        String token = ((HttpServletRequest) request).getHeader("Auth");

        if (token != null && tokenService.verifyToken(token)) {
            String email = tokenService.getUid(token);

            //TODO: 아니면 여기서 회원가입 처리..?
            //일단은 실습을 위해 만들어서 사용하자. 나중에 내가 스스로 구현 ㄱ
            UserDto userDto = UserDto.builder()
                    .email(email)
                    .name("name")
                    .profileImg("profileImg")
                    .build();

            Authentication auth = getAuthentication(userDto);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

    }

    public Authentication getAuthentication(UserDto member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
