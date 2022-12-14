package com.example.siderswebapp.auth.oauth.handler;

import static com.example.siderswebapp.auth.jwt.JwtProvider.AUTH_ID_KEY;
import static com.example.siderswebapp.auth.oauth.handler.RedirectUri.*;

import com.example.siderswebapp.auth.jwt.JwtProvider;
import com.example.siderswebapp.member.domain.AuthId;
import com.example.siderswebapp.member.domain.Member;
import com.example.siderswebapp.member.domain.repository.MemberRepository;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final int COOKIE_MAX_AGE = 3600;

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        Optional<Member> findMember = memberRepository.findByAuthId(
                new AuthId(getAuthId(oauth2User)));

        addJwtToCookie(oauth2User, response);

        if (findMember.isEmpty()) {
            String redirectionUri = LOGIN_FAIL.redirectUri();
            response.sendRedirect(redirectionUri);
            return;
        }

        Member member = findMember.get();
        member.saveRefreshToken(jwtProvider.generateRefreshToken());

        String redirectionUri = LOGIN_SUCCESS.redirectUri();
        response.sendRedirect(redirectionUri);
    }

    private void addJwtToCookie(OAuth2User oauth2User, HttpServletResponse response) {
        final String COOKIE_NAME = "token";
        String accessToken = jwtProvider.generateAccessToken(oauth2User);

        Cookie cookie = new Cookie(COOKIE_NAME, accessToken);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    private String getAuthId(OAuth2User oauth2User) {
        Map<String, Object> attributes = oauth2User.getAttributes();
        return (String) attributes.get(AUTH_ID_KEY);
    }
}
