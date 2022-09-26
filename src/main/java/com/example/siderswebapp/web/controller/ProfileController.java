package com.example.siderswebapp.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final Environment env;

    @GetMapping("/profile")
    public String profile() {
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> ec2Profiles = Arrays.asList("ec2", "ec2-nginx1", "ec2-nginx2");

        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);

        return profiles.stream()
                .filter(ec2Profiles::contains)  // 환경 변수에 있는 profile 목록에 ec2Pofile이 있니? -> 있는 애들만 걸러냄.
                .findAny()
                .orElse(defaultProfile);  // 없으면 defaultProfile을 반환.
    }
}
