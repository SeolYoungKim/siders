package com.example.siderswebapp.auth.jwt;

import com.nimbusds.oauth2.sdk.token.AccessTokenType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    private final static String AUTHORITIES_KEY = "authority";
    private final static String BEARER_TYPE = "Bearer";

    @Value("${jwt.access-token-expire-time}")
    private long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${jwt.refresh-token-expire-time}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    // 빈 생성 및 생성자 주입이 다 끝난 후에 Key를 생성
    @PostConstruct
    private void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 액세스 토큰 발급
    public String generateAccessToken(OAuth2User oAuth2User) {
        long now = new Date().getTime();
        Date accessTokenExpireTime = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Authorities를 Claim에 넣을 수 있도록 String으로 변경 (authority1,authority2,..)
        String authorities = oAuth2User.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject((String) attributes.get("id"))
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpireTime)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // 리프레시 토큰 발급
    public String generateRefreshToken() {
        long now = new Date().getTime();
        Date refreshTokenExpireTime = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setExpiration(refreshTokenExpireTime)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }


    // 요청 헤더에서 토큰 꺼내오기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AccessTokenType.BEARER.getValue())) {
            return bearerToken.substring(7);
        }

        return "";
    }

    // 토큰 검증하기
    public Boolean validateToken(String token) {

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("올바르지 못한 토큰입니다.");
        } catch (ExpiredJwtException e) {
            // TODO: 만료된 토큰이면 재발급을 해줘야...
            log.info("만료된 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("잘못된 토큰입니다.");
        }

        return false;
    }

    // 액세스 토큰으로 Authentication 만들기
    public Authentication authenticate(String accessToken) {
        Claims claims = parseClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        User user = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(user, "", authorities);
    }

    // Claim을 파싱하는 메서드
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}
