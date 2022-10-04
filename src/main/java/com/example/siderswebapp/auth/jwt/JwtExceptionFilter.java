package com.example.siderswebapp.auth.jwt;

import com.example.siderswebapp.exception.JwtNotAvailable;
import com.example.siderswebapp.web.response.exception.ErrorResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (JwtNotAvailable e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ErrorResult errorResult = ErrorResult.builder()
                    .code(e.getErrorCode())
                    .status(e.getStatus())
                    .message(e.getMessage())
                    .build();

            objectMapper.writeValue(response.getWriter(), errorResult);
        }
    }
}
