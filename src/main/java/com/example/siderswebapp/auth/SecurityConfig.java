package com.example.siderswebapp.auth;

import com.example.siderswebapp.auth.jwt.JwtExceptionFilter;
import com.example.siderswebapp.auth.jwt.JwtFilter;
import com.example.siderswebapp.auth.jwt.JwtProvider;
import com.example.siderswebapp.auth.oauth.handler.OAuth2LoginSuccessHandler;
import com.example.siderswebapp.auth.oauth.service.CustomUserDetailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static com.example.siderswebapp.auth.UriList.FRONT_END;
import static org.springframework.security.config.Customizer.withDefaults;

//TODO: 구현한 코드 내용 정리

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers(
                "/h2-console/**",
                "/favicon.ico",
                "/error",
                "/docs/sidersApi.html"
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)

                .and()
                .cors(withDefaults())

                .authorizeRequests(expressionInterceptUrlRegistry -> expressionInterceptUrlRegistry
                        .mvcMatchers("/api2/posts").permitAll()  // TODO Test용
                        .mvcMatchers("/api2/search").permitAll() // TODO Test용
                        .mvcMatchers("/api/posts").permitAll()
                        .mvcMatchers("/api/search").permitAll()
                        .mvcMatchers(HttpMethod.GET, "/api2/member").permitAll()  // TODO Test용
                        .mvcMatchers(HttpMethod.GET, "/api2/post/**").permitAll() // TODO Test용
                        .mvcMatchers(HttpMethod.GET, "/api/member").permitAll()
                        .mvcMatchers(HttpMethod.GET, "/api/post/**").permitAll()
                        .anyRequest().authenticated())

                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer
                        .loginPage(UriList.FRONT_END.getUri())
                        .userInfoEndpoint()
                        .userService(customUserDetailService)
                        .and()
                        .successHandler(oAuth2LoginSuccessHandler))

                .addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(objectMapper), JwtFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("https://admirable-starburst-00d6a9.netlify.app/", FRONT_END.getUri()));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
