package com.example.siderswebapp.config.oauth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
public class TokenService {


    // access token 서명
    // access token에서 ID와 권한 부여 claim 추출 -> UserContext 생성
    // 토큰이 잘못되었거나, 만료되었거나, 적절한 서명 키로 서명되지 않은 경우 인증 예외 발생

    private String secretKey = "token-secret-key";

    @PostConstruct
    private void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    public Token generateToken(String uid, String role) {
        long tokenPeriod = 1000L * 60 * 10;
        long refreshPeriod = 1000L * 60 * 60 * 24 * 30 * 3;

        // TODO: 필요한 정보를 담는다.
        Claims claims = Jwts.claims().setSubject(uid);
        claims.put("role", role);

        Date now = new Date();

        return Token.builder()
                .token(Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + tokenPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact())
                .refreshToken(Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + refreshPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact())
                .build();

    }


    public Boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)  // sign을 진행했던 키로 암호를 푼다.
                    .parseClaimsJws(token);

            return claims.getBody()
                    .getExpiration()
                    .after(new Date());  // 기간이 지났니? 안지났니?
        } catch (Exception e) {
            return false;
        }
    }

    public String getUid(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();  // sub 에 email이 존재하도록 설정을 해두었기 때문에, getUid를 하면 email을 뱉는다.
    }

}
