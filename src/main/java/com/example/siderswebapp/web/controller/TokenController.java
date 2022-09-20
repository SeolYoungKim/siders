package com.example.siderswebapp.web.controller;

import com.example.siderswebapp.config.oauth.jwt.Token;
import com.example.siderswebapp.config.oauth.jwt.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;

    // Test
    @GetMapping("/test")
    public String test() {
        return "Jwt는...tq";
    }

    @GetMapping("/token/expired")
    public String auth() {
        throw new RuntimeException();
    }

    @GetMapping("/token/refresh")
    public String refreshAuth(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader("Refresh");

        if (refreshToken != null && tokenService.verifyToken(refreshToken)) {
            String email = tokenService.getUid(refreshToken);
            Token newToken = tokenService.generateToken(email, "USER");

            response.addHeader("Auth", newToken.getToken());
            response.addHeader("Refresh", newToken.getRefreshToken());
            response.setContentType("application/json;charset=UTF-8");

            return "새로운 토큰이 발급되었습니다.";
        }

        throw new RuntimeException();
    }
}
