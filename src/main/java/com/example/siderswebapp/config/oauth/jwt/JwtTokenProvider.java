package com.example.siderswebapp.config.oauth.jwt;

import com.example.siderswebapp.repository.member.MemberRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final MemberRepository memberRepository;

    // access token 서명
    // access token에서 ID와 권한 부여 claim 추출 -> UserContext 생성
    // 토큰이 잘못되었거나, 만료되었거나, 적절한 서명 키로 서명되지 않은 경우 인증 예외 발생

    private final String SECRET_KEY;
    private final String COOKIE_REFRESH_TOKEN_KEY;

    private final String AUTHORITIES_KEY = "role";
    private final String BEARER_TYPE = "bearer";

    private final Integer ACCESS_TOKEN_EXPIRE_TIME = (1000 * 60 * 30);
    private final Integer REFRESH_TOKEN_EXPIRE_TIME = (1000 * 60 * 60 * 24 * 7);

    public JwtTokenProvider(@Value("${app.auth.token.secret-key}") String secretKey,
                            @Value("${app.auth.token.refresh-cookie-key}") String cookieKey) {

        this.SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.COOKIE_REFRESH_TOKEN_KEY = cookieKey;
    }

    public String generateAccessToken(Authentication authentication) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME);

        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        String name = user.getName();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(name)
                .claim(AUTHORITIES_KEY, role)
                .setIssuer("debrains")
                .setIssuedAt(now)
                .setExpiration(validity)
                .compact();


    }

    public String generateRefreshToken(Authentication authentication, HttpServletResponse response) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME);

        String refreshToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setIssuer("debrains")
                .setIssuedAt(now)
                .setExpiration(validity)
                .compact();

        saveRefreshToken(authentication, refreshToken);

        ResponseCookie cookie = ResponseCookie.from(COOKIE_REFRESH_TOKEN_KEY, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Lax")
                .maxAge(REFRESH_TOKEN_EXPIRE_TIME / 1000)
                .path("/")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void saveRefreshToken(Authentication authentication, String refreshToken) {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        Long id = Long.valueOf(user.getName());

        memberRepository.updateRefreshToken(id, refreshToken);
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        //TODO: https://europani.github.io/spring/2022/01/15/036-oauth2-jwt.html#h-authprovider
        // CustomUserDetails 생성해서 해봐야 할듯..?
        // 아래의 글도 다시 읽기
        // https://github.com/murraco/spring-boot-jwt/tree/master/src/main/java/murraco/security
        DefaultOAuth2User defaultOAuth2User = new DefaultOAuth2User(authorities);
        return null;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String resolveToken(HttpServletRequest request) {
        /* ... */
        return "";

    }



    /* ... */

}
