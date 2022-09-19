package com.example.siderswebapp.config.oauth;

import com.example.siderswebapp.config.oauth.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인이 성공하면 호출되는 핸들러
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${app.oauth2.authorizedRedirectUri}")
    private String redirectUri;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String targetUri = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.debug("Response가 이미 Commit 되었습니다.");
            return;
        }

        clearAuthenticationAttributes(request);  // 세션에 존재할 수도 있는 임시 데이터 삭제
        getRedirectStrategy().sendRedirect(request, response, targetUri);

    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String targetUrl = redirectUri;

        // JWT 토큰 생성
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        jwtTokenProvider.generateRefreshToken(authentication, response);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("accessToken", accessToken)
                .build().toUriString();
    }
}
