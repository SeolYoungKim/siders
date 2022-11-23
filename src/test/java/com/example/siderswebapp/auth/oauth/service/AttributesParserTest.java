package com.example.siderswebapp.auth.oauth.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AttributesParserTest {

    private final Map<String, Object> expected = Map.of(
            "id", "authId",
            "sub", "email@email.com",
            "name", "name",
            "picture", "picture"
    );

    @ParameterizedTest(name = "{0} 로그인 정보가 들어오면 authId, name, email, picture를 파싱 해준다.")
    @MethodSource("defaultOAuth2ProviderAndAttributes")
    void parseToOAuth2Attributes(String registrationId, Map<String, Object> attributes) {
        Map<String, Object> actual = AttributesParser.parseToOAuth2Attributes(
                registrationId, attributes);

        assertThat(actual).containsExactlyEntriesOf(expected);
    }

    @DisplayName("naver 로그인 정보가 들어오면 authId, name, email, picture를 파싱 해준다.")
    @Test
    void parseToOAuth2AttributesForNaver() {
        Map<String, Object> naverAttrs = Map.of(
                "response", Map.of(
                        "id", "authId",
                        "email", "email@email.com",
                        "name", "name",
                        "profile_image", "picture"
                )
        );

        Map<String, Object> actual = AttributesParser.parseToOAuth2Attributes(
                "naver", naverAttrs);

        assertThat(actual).containsExactlyEntriesOf(expected);
    }

    @DisplayName("kakao 로그인 정보가 들어오면 authId, name, email, picture를 파싱 해준다.")
    @Test
    void parseToOAuth2AttributesForKakao() {
        Map<String, Object> kakaoAttrs = Map.of(
                "id", "authId",
                "kakao_account", Map.of(
                        "nickname", "name",
                        "profile", Map.of(
                                "email", "email@email.com",
                                "profile_image_url", "picture"
                        )
                )
        );

        Map<String, Object> actual = AttributesParser.parseToOAuth2Attributes(
                "kakao", kakaoAttrs);

        assertThat(actual).containsExactlyEntriesOf(expected);
    }



    private static Stream<Arguments> defaultOAuth2ProviderAndAttributes() {
        return Stream.of(
                Arguments.of("google",
                        Map.of(
                                "sub", "authId",
                                "email", "email@email.com",
                                "name", "name",
                                "picture", "picture"
                        )),
                Arguments.of("github",
                        Map.of(
                                "id", "authId",
                                "login", "email@email.com",
                                "name", "name",
                                "avatar_url", "picture"
                        )
                ));
    }
}