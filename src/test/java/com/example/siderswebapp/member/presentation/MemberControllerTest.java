package com.example.siderswebapp.member.presentation;

import static com.example.siderswebapp.TestAttributes.TEST_ATTRIBUTES;
import static com.example.siderswebapp.post.domain.Ability.LOW;
import static com.example.siderswebapp.post.domain.RecruitType.PROJECT;
import static com.example.siderswebapp.post.domain.RecruitType.STUDY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.siderswebapp.member.application.dto.SignUpDto;
import com.example.siderswebapp.member.domain.Member;
import com.example.siderswebapp.member.domain.RoleType;
import com.example.siderswebapp.member.domain.repository.MemberRepository;
import com.example.siderswebapp.post.domain.Fields;
import com.example.siderswebapp.post.domain.Post;
import com.example.siderswebapp.post.domain.TechStack;
import com.example.siderswebapp.post.domain.repository.fields.FieldsRepository;
import com.example.siderswebapp.post.domain.repository.post.PostRepository;
import com.example.siderswebapp.post.domain.repository.tech_stack.TechStackRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

//TODO: ????????????????????? ?????? ??? ??? ??????????????? ??? ??? ??????.. ?????? ?????? ???????????? ?????? ??????????????????!

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private FieldsRepository fieldsRepository;
    @Autowired
    private TechStackRepository techStackRepository;

    @DisplayName("?????? ????????? ????????????.")
    @Test
    void signupOk() throws Exception {
        SignUpDto signUpDto = new SignUpDto("???????????????");

        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto))
                        .with(user("authId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authId").value("authId"))
                .andExpect(jsonPath("$.name").value("???????????????"))
                .andExpect(jsonPath("$.picture").value(""))
                .andExpect(jsonPath("$.accessToken").exists())
                .andDo(print());

        assertThat(memberRepository.findAll().size()).isEqualTo(1);
    }

    @DisplayName("authId??? ?????? ????????? ?????? ????????? ?????? ????????? ?????????")
    @Test
    void signupFalse() throws Exception {
        Member member = Member.builder()
                .authId("authId")
                .picture("savedPicture")
                .name("savedName")
                .email("savedEmail")
                .refreshToken("savedRefreshToken")
                .roleType(RoleType.USER)
                .build();

        memberRepository.save(member);

        SignUpDto signUpDto = new SignUpDto("???????????????");

        mockMvc.perform(post("/api/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto))
                        .with(user("authId").password("").roles("USER")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("?????? ???????????? ???????????????."))
                .andExpect(jsonPath("$.code").value("ILLEGAL_ARGS-ERR-400"))
                .andDo(print());

        assertThat(memberRepository.findAll().size()).isEqualTo(1);
    }

    @DisplayName("?????? ???????????? ?????? isExists=true ??? ????????????.")
    @Test
    void duplicateNameCheck() throws Exception {
        Member member = Member.builder()
                .authId("savedAuthId")
                .picture("savedPicture")
                .name("savedName")
                .email("savedEmail")
                .refreshToken("savedRefreshToken")
                .roleType(RoleType.USER)
                .build();

        memberRepository.save(member);

        mockMvc.perform(get("/api/signup?name=savedName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("authId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isExists").value(true))
                .andDo(print());
    }

    @DisplayName("?????? ???????????? ?????? ?????? isExists=false ??? ????????????.")
    @Test
    void duplicateNameCheck2() throws Exception {
        Member member = Member.builder()
                .authId("savedAuthId")
                .picture("savedPicture")
                .name("savedName")
                .email("savedEmail")
                .refreshToken("savedRefreshToken")
                .roleType(RoleType.USER)
                .build();

        memberRepository.save(member);

        Map<String, Object> attributes = TEST_ATTRIBUTES.getAttributes();

        mockMvc.perform(get("/api/signup?name=haha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("authId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isExists").value(false))
                .andDo(print());
    }

    @DisplayName("????????? ????????? ????????? ??? ??????.")
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

    @DisplayName("?????? ????????? ??????.")
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


        Post post = Post.builder()
                .title("??????")
                .recruitType(STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("???????????????")
                .expectedPeriod("1??????")
                .isCompleted(false)
                .member(member)
                .build();

        Fields back = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(LOW)
                .post(post)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        memberRepository.save(member);
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

    @DisplayName("????????? ????????? ???????????? ????????? ??? ??????.")
    @Test
    void memberPost() throws Exception {
        Member member = Member.builder()
                .authId("savedAuthId")
                .picture("savedPicture")
                .name("savedName")
                .email("savedEmail")
                .refreshToken("savedRefreshToken")
                .roleType(RoleType.USER)
                .build();

        Post post = Post.builder()
                .title("??????")
                .recruitType(STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("???????????????")
                .expectedPeriod("1??????")
                .isCompleted(false)
                .member(member)
                .build();

        Fields back = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(LOW)
                .post(post)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        memberRepository.save(member);
        postRepository.save(post);

        Member otherMember = Member.builder()
                .authId("otherId")
                .picture("otherPicture")
                .name("otherName")
                .email("otherEmail")
                .refreshToken("otherRefreshToken")
                .roleType(RoleType.USER)
                .build();

        Post otherPost = Post.builder()
                .title("????????? ?????? ????????? ??? ???")
                .recruitType(PROJECT)
                .contact("020.0000.0000")
                .recruitIntroduction("???????????????2")
                .expectedPeriod("100??????")
                .isCompleted(false)
                .member(otherMember)
                .build();

        memberRepository.save(otherMember);
        postRepository.save(otherPost);

        mockMvc.perform(get("/api/member/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberPostList.[0].id").value(post.getId()))
                .andExpect(jsonPath("$.memberPostList.[0].title").value("??????"))
                .andDo(print());
    }

    @DisplayName("????????? ????????? ??????????????? ?????? ????????? ??? ??????.")
    @Test
    void memberPost2() throws Exception {
        Member member = Member.builder()
                .authId("savedAuthId")
                .picture("savedPicture")
                .name("savedName")
                .email("savedEmail")
                .refreshToken("savedRefreshToken")
                .roleType(RoleType.USER)
                .build();

        List<Post> postList = IntStream.range(1, 11)
                .mapToObj(i -> Post.builder()
                        .member(member)
                        .isCompleted(false)
                        .expectedPeriod("1??????")
                        .title("title " + i)
                        .recruitType(i % 2 == 0 ? PROJECT : STUDY)
                        .contact("email " + i)
                        .recruitIntroduction("contents " + i)
                        .build())
                .collect(Collectors.toList());

        memberRepository.save(member);
        postRepository.saveAll(postList);

        mockMvc.perform(get("/api/member/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberPostList.size()").value(10))
                .andDo(print());
    }
}














