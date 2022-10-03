package com.example.siderswebapp.web.controller;

import com.example.siderswebapp.domain.fields.Fields;
import com.example.siderswebapp.domain.member.Member;
import com.example.siderswebapp.domain.member.RoleType;
import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.domain.tech_stack.TechStack;
import com.example.siderswebapp.repository.fields.FieldsRepository;
import com.example.siderswebapp.repository.member.MemberRepository;
import com.example.siderswebapp.repository.post.PostRepository;
import com.example.siderswebapp.repository.tech_stack.TechStackRepository;
import com.example.siderswebapp.web.request.member.SignUpDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.example.siderswebapp.domain.Ability.LOW;
import static com.example.siderswebapp.domain.RecruitType.STUDY;
import static com.example.siderswebapp.web.controller.attributes.TestAttributes.TEST_ATTRIBUTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//TODO: 단위테스트라는 것을 좀 더 고민해봐야 할 것 같다.. 일단 내가 작성하는 것은 통합테스트임!

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired private MemberRepository memberRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private FieldsRepository fieldsRepository;
    @Autowired private TechStackRepository techStackRepository;

    @DisplayName("회원 가입이 수행된다.")
    @Test
    void signupOk() throws Exception {
        SignUpDto signUpDto = new SignUpDto("유저닉네임");

        Map<String, Object> attributes = TEST_ATTRIBUTES.getAttributes();

        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto))
                        .with(oauth2Login().attributes(attrs -> attrs.putAll(attributes))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authId").value("authId"))
                .andExpect(jsonPath("$.name").value("유저닉네임"))
                .andExpect(jsonPath("$.picture").value("picture"))
                .andExpect(jsonPath("$.accessToken").exists())
                .andDo(print());

        assertThat(memberRepository.findAll().size()).isEqualTo(1);
    }

    @DisplayName("유저의 정보를 조회할 수 있다.")
    @Test
    void memberInfo() throws Exception {
        Member member = Member.builder()
                .authId("savedAuthId")
                .picture("savedPicture")
                .name("savedName")
                .email("savedEmail")
                .refreshToken("savedRefreshToken")
                .roleType(RoleType.USER)
                .build();

        memberRepository.save(member);

        mockMvc.perform(get("/api/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authId").value("savedAuthId"))
                .andExpect(jsonPath("$.picture").value("savedPicture"))
                .andExpect(jsonPath("$.name").value("savedName"))
                .andDo(print());
    }

    @DisplayName("회원 탈퇴가 된다.")
    @Test
    void deleteMember() throws Exception {
        Member member = Member.builder()
                .authId("savedAuthId")
                .picture("savedPicture")
                .name("savedName")
                .email("savedEmail")
                .refreshToken("savedRefreshToken")
                .roleType(RoleType.USER)
                .build();

        Member savedMember = memberRepository.save(member);

        Post post = Post.builder()
                .title("제목")
                .recruitType(STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할사람")
                .expectedPeriod("1개월")
                .isCompleted(false)
                .member(savedMember)
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(LOW)
                .post(post)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        postRepository.save(post);

        mockMvc.perform(delete("/api/member")
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andDo(print());

        assertThat(memberRepository.findAll()).isEmpty();
        assertThat(postRepository.findAll()).isEmpty();
        assertThat(fieldsRepository.findAll()).isEmpty();
        assertThat(techStackRepository.findAll()).isEmpty();
    }

}














