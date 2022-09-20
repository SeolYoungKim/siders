package com.example.siderswebapp.config.oauth;

import com.example.siderswebapp.config.oauth.handler.OAuth2SuccessHandler;
import com.example.siderswebapp.config.oauth.jwt.JwtAuthFilter;
import com.example.siderswebapp.config.oauth.jwt.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import static com.example.siderswebapp.domain.member.Role.USER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final TokenService tokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Jwt token을 이용할 계획이므로, 상태 유지가 안되기 때문에 필요 없다고 사료됨.
                .headers().frameOptions().sameOrigin()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/", "/css/**", "/images/**", "/js/**",
                                "/h2-console/**", "/profile", "/post/**", "/token/**").permitAll()
                        .antMatchers("/recruitment", "/setting", "/myPosts").hasRole(USER.name())
                        .anyRequest().authenticated())
                .oauth2Login(oauth2Login -> oauth2Login
                        .successHandler(oAuth2SuccessHandler)
                        .userInfoEndpoint()
                        .userService(customOAuth2UserService))
                .logout(logout -> logout
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/"))
                .addFilterBefore(new JwtAuthFilter(tokenService), LogoutFilter.class);


        return http.build();

    }

}
