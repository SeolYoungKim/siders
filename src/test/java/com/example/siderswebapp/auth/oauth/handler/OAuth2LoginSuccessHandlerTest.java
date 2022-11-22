package com.example.siderswebapp.auth.oauth.handler;

import com.example.siderswebapp.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class OAuth2LoginSuccessHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("인증이 완료되면 제대로 리디렉션이 발생한다.")
    @Test
    void oauth2Test() throws Exception {
        //TODO: SuccessHandler를 테스트 할 방법이 없을까?
    }

}