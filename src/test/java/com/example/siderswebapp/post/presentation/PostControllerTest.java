package com.example.siderswebapp.post.presentation;

import static com.example.siderswebapp.post.domain.Ability.HIGH;
import static com.example.siderswebapp.post.domain.Ability.LOW;
import static com.example.siderswebapp.post.domain.Ability.MID;
import static com.example.siderswebapp.post.domain.RecruitType.PROJECT;
import static com.example.siderswebapp.post.domain.RecruitType.STUDY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.siderswebapp.member.domain.Member;
import com.example.siderswebapp.member.domain.RoleType;
import com.example.siderswebapp.member.domain.repository.MemberRepository;
import com.example.siderswebapp.post.application.dto.completion.IsCompletedDto;
import com.example.siderswebapp.post.application.dto.create.CreateFieldsRequest;
import com.example.siderswebapp.post.application.dto.create.CreatePostRequest;
import com.example.siderswebapp.post.application.dto.create.CreateTechStackRequest;
import com.example.siderswebapp.post.application.dto.update.UpdateFieldsRequest;
import com.example.siderswebapp.post.application.dto.update.UpdatePostRequest;
import com.example.siderswebapp.post.application.dto.update.UpdateTechStackRequest;
import com.example.siderswebapp.post.domain.Fields;
import com.example.siderswebapp.post.domain.Post;
import com.example.siderswebapp.post.domain.TechStack;
import com.example.siderswebapp.post.domain.repository.fields.FieldsRepository;
import com.example.siderswebapp.post.domain.repository.post.PostRepository;
import com.example.siderswebapp.post.domain.repository.tech_stack.TechStackRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
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

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired private PostRepository postRepository;
    @Autowired private FieldsRepository fieldsRepository;
    @Autowired private TechStackRepository techStackRepository;
    @Autowired private MemberRepository memberRepository;

    @DisplayName("??? ?????? ???, ???????????? ??? ????????????.")
    @Test
    void recruitmentTest() throws Exception {

        List<CreateTechStackRequest> designStack = IntStream.range(1, 4)
                .mapToObj(i -> new CreateTechStackRequest("???????????????" + i))
                .collect(Collectors.toList());

        List<CreateTechStackRequest> frontendStack = IntStream.range(1, 4)
                .mapToObj(i -> new CreateTechStackRequest("?????????????????????" + i))
                .collect(Collectors.toList());

        List<CreateTechStackRequest> backendStack = IntStream.range(1, 4)
                .mapToObj(i -> new CreateTechStackRequest("???????????????" + i))
                .collect(Collectors.toList());

        CreateFieldsRequest design = CreateFieldsRequest.builder()
                .fieldsName("?????????")
                .recruitCount(2)
                .totalAbility("Low")
                .stacks(new ArrayList<>())
                .build();

        CreateFieldsRequest frontend = CreateFieldsRequest.builder()
                .fieldsName("???????????????")
                .recruitCount(3)
                .totalAbility("Mid")
                .stacks(new ArrayList<>())
                .build();

        CreateFieldsRequest backend = CreateFieldsRequest.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility("High")
                .stacks(new ArrayList<>())
                .build();

        CreatePostRequest post = CreatePostRequest.builder()
                .title("??????")
                .recruitType("study")
                .contact("010-0000-1111")
                .recruitIntroduction("????????? ???????????? ?????????")
                .expectedPeriod("1??????")
                .fieldsList(new ArrayList<>())
                .build();

        post.getFieldsList().add(design);
        post.getFieldsList().add(frontend);
        post.getFieldsList().add(backend);

        design.getStacks().addAll(designStack);
        frontend.getStacks().addAll(frontendStack);
        backend.getStacks().addAll(backendStack);

        Member member = Member.builder()
                .authId("savedAuthId")
                .picture("savedPicture")
                .name("savedName")
                .email("savedEmail")
                .refreshToken("savedRefreshToken")
                .roleType(RoleType.USER)
                .build();

        memberRepository.save(member);

        mockMvc.perform(post("/api/recruitment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post))
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").exists())
                .andDo(print());

        assertThat(postRepository.findAll().size()).isEqualTo(1);
        assertThat(fieldsRepository.findAll().size()).isEqualTo(3);
        assertThat(techStackRepository.findAll().size()).isEqualTo(9);
    }

    @DisplayName("????????? ?????? ????????? ?????? ????????? ?????? ????????? ?????????.")
    @Test
    void recruitmentTest2() throws Exception {

        List<CreateTechStackRequest> designStack = IntStream.range(1, 4)
                .mapToObj(i -> new CreateTechStackRequest("???????????????" + i))
                .collect(Collectors.toList());

        List<CreateTechStackRequest> frontendStack = IntStream.range(1, 4)
                .mapToObj(i -> new CreateTechStackRequest("?????????????????????" + i))
                .collect(Collectors.toList());

        List<CreateTechStackRequest> backendStack = IntStream.range(1, 4)
                .mapToObj(i -> new CreateTechStackRequest("???????????????" + i))
                .collect(Collectors.toList());

        CreateFieldsRequest design = CreateFieldsRequest.builder()
                .fieldsName("?????????")
                .recruitCount(2)
                .totalAbility("Low")
                .stacks(new ArrayList<>())
                .build();

        CreateFieldsRequest frontend = CreateFieldsRequest.builder()
                .fieldsName("???????????????")
                .recruitCount(3)
                .totalAbility("Mid")
                .stacks(new ArrayList<>())
                .build();

        CreateFieldsRequest backend = CreateFieldsRequest.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility("High")
                .stacks(new ArrayList<>())
                .build();

        CreatePostRequest post = CreatePostRequest.builder()
                .title("??????")
                .recruitType("study")
                .contact("010-0000-1111")
                .recruitIntroduction("????????? ???????????? ?????????")
                .expectedPeriod("1??????")
                .fieldsList(new ArrayList<>())
                .build();

        post.getFieldsList().add(design);
        post.getFieldsList().add(frontend);
        post.getFieldsList().add(backend);

        design.getStacks().addAll(designStack);
        frontend.getStacks().addAll(frontendStack);
        backend.getStacks().addAll(backendStack);

        mockMvc.perform(post("/api/recruitment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post))
                        .with(user("????????????").password("").roles("USER")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("???????????? ?????? ???????????????."))
                .andExpect(jsonPath("$.code").value("MEMBER-ERR-404"))
                .andDo(print());
    }

    @DisplayName("??? ???????????? ?????? ???????????? isWriter??? true???.")
    @Test
    void sameUserReadPostTest() throws Exception {

        Member member = Member.builder()
                .authId("savedAuthId")
                .picture("savedPicture")
                .name("savedName")
                .email("savedEmail")
                .refreshToken("savedRefreshToken")
                .roleType(RoleType.USER)
                .build();

        memberRepository.save(member);

        Post post = Post.builder()
                .title("??????")
                .recruitType(STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("???????????????")
                .expectedPeriod("1??????")
                .member(member)
                .isCompleted(false)
                .build();

        Fields design = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(3)
                .totalAbility(LOW)
                .post(post)
                .build();

        Fields front = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(MID)
                .post(post)
                .build();

        Fields back = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(HIGH)
                .post(post)
                .build();

        TechStack zeplin = TechStack.builder()
                .stackName("zeplin")
                .fields(design)
                .build();

        TechStack react = TechStack.builder()
                .stackName("react")
                .fields(front)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        Post savedPost = postRepository.save(post);

        mockMvc.perform(get("/api/post/{id}", savedPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("??????"))
                .andExpect(jsonPath("$.recruitType").value("STUDY"))
                .andExpect(jsonPath("$.recruitIntroduction").value("???????????????"))
                .andExpect(jsonPath("$.expectedPeriod").value("1??????"))
                .andExpect(jsonPath("$.isWriter").value(true))
                .andExpect(jsonPath("$.fieldsList.[0].fieldsName").value("?????????"))
                .andExpect(jsonPath("$.fieldsList.[0].stacks.[0].stackName").value("zeplin"))
                .andDo(print());
    }

    @DisplayName("??? ???????????? ?????? ????????? ?????? ???????????? isWriter??? false???.")
    @Test
    void notSameUserReadPostTest() throws Exception {

        Member member = Member.builder()
                .authId("savedAuthId")
                .picture("savedPicture")
                .name("savedName")
                .email("savedEmail")
                .refreshToken("savedRefreshToken")
                .roleType(RoleType.USER)
                .build();

        memberRepository.save(member);

        Post post = Post.builder()
                .title("??????")
                .recruitType(STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("???????????????")
                .expectedPeriod("1??????")
                .member(member)
                .isCompleted(false)
                .build();

        Fields design = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(3)
                .totalAbility(LOW)
                .post(post)
                .build();

        Fields front = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(MID)
                .post(post)
                .build();

        Fields back = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(HIGH)
                .post(post)
                .build();

        TechStack zeplin = TechStack.builder()
                .stackName("zeplin")
                .fields(design)
                .build();

        TechStack react = TechStack.builder()
                .stackName("react")
                .fields(front)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        Post savedPost = postRepository.save(post);

        mockMvc.perform(get("/api/post/{id}", savedPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("notSameUser").password("").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("??????"))
                .andExpect(jsonPath("$.recruitType").value("STUDY"))
                .andExpect(jsonPath("$.recruitIntroduction").value("???????????????"))
                .andExpect(jsonPath("$.expectedPeriod").value("1??????"))
                .andExpect(jsonPath("$.isWriter").value(false))
                .andExpect(jsonPath("$.fieldsList.[0].fieldsName").value("?????????"))
                .andExpect(jsonPath("$.fieldsList.[0].stacks.[0].stackName").value("zeplin"))
                .andDo(print());
    }

    @DisplayName("????????? ???????????? ?????? ????????? ??? ??????.")
    @Test
    void notMemberReadPostTest() throws Exception {

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
                .title("??????")
                .recruitType(STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("???????????????")
                .expectedPeriod("1??????")
                .member(savedMember)
                .isCompleted(false)
                .build();

        Fields design = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(3)
                .totalAbility(LOW)
                .post(post)
                .build();

        Fields front = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(MID)
                .post(post)
                .build();

        Fields back = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(HIGH)
                .post(post)
                .build();

        TechStack zeplin = TechStack.builder()
                .stackName("zeplin")
                .fields(design)
                .build();

        TechStack react = TechStack.builder()
                .stackName("react")
                .fields(front)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        Post savedPost = postRepository.save(post);

        mockMvc.perform(get("/api/post/{id}", savedPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("notSameUser").password("").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("??????"))
                .andExpect(jsonPath("$.recruitType").value("STUDY"))
                .andExpect(jsonPath("$.recruitIntroduction").value("???????????????"))
                .andExpect(jsonPath("$.expectedPeriod").value("1??????"))
                .andExpect(jsonPath("$.isWriter").value(false))
                .andExpect(jsonPath("$.fieldsList.[0].fieldsName").value("?????????"))
                .andExpect(jsonPath("$.fieldsList.[0].stacks.[0].stackName").value("zeplin"))
                .andDo(print());
    }

    @DisplayName("????????? ????????? - ????????? ???????????? ?????? ?????? ????????????.")
    @Test
    void testPaging() throws Exception {
        Member member = Member.builder()
                .authId("savedAuthId")
                .picture("savedPicture")
                .name("savedName")
                .email("savedEmail")
                .refreshToken("savedRefreshToken")
                .roleType(RoleType.USER)
                .build();

        Member savedMember = memberRepository.save(member);

        List<Post> postList = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("title " + i)
                        .recruitType(STUDY)
                        .contact("email")
                        .recruitIntroduction("content " + i)
                        .expectedPeriod(i + "??????")
                        .member(savedMember)
                        .isCompleted(false)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(postList);

        Post post = Post.builder()
                .isCompleted(true)
                .recruitType(PROJECT)
                .contact("333")
                .recruitIntroduction("intro")
                .member(savedMember)
                .title("title")
                .build();

        postRepository.save(post);

        mockMvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].title").value("title 30"))
                .andExpect(jsonPath("$.content.[9].title").value("title 21"))
                .andDo(print());
    }

    @DisplayName("????????? ????????? - ?????? ?????? ?????? ?????? ?????? ?????? ???????????? ?????????.")
    @Test
    void testPaging2() throws Exception {

        Member member = Member.builder()
                .authId("savedAuthId")
                .picture("savedPicture")
                .name("savedName")
                .email("savedEmail")
                .refreshToken("savedRefreshToken")
                .roleType(RoleType.USER)
                .build();

        Member savedMember = memberRepository.save(member);

        List<Post> postList = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("title " + i)
                        .recruitType(STUDY)
                        .contact("email")
                        .recruitIntroduction("content " + i)
                        .expectedPeriod(i + "??????")
                        .member(savedMember)
                        .isCompleted(true)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(postList);

        mockMvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0]").doesNotExist())
                .andDo(print());

    }

    @DisplayName("????????? ????????? - ????????? ????????? ??? ????????????.")
    @Test
    void testPaging3() throws Exception {
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
                .title("??????")
                .recruitType(STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("???????????????")
                .expectedPeriod("1??????")
                .member(savedMember)
                .isCompleted(false)
                .build();

        Fields back = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(HIGH)
                .post(post)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        postRepository.save(post);

        mockMvc.perform(get("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].title").value("??????"))
                .andExpect(jsonPath("$.content.[0].recruitType").value("STUDY"))
                .andExpect(jsonPath("$.content.[0].fieldsList.[0].fieldsName").value("?????????"))
                .andExpect(jsonPath("$.content.[0].fieldsList.[0].recruitCount").value(1))
                .andExpect(jsonPath("$.content.[0].fieldsList.[0].stacks.[0].stackName").value("spring"))
                .andDo(print());
    }

    @DisplayName("Search test - ?????? ??? ????????????.")
    @Test
    void testSearching() throws Exception {
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
                .title("??????")
                .recruitType(STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("???????????????")
                .expectedPeriod("1??????")
                .member(savedMember)
                .isCompleted(false)
                .build();

        Fields design = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(3)
                .totalAbility(LOW)
                .post(post)
                .build();

        Fields front = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(MID)
                .post(post)
                .build();

        Fields back = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(HIGH)
                .post(post)
                .build();

        TechStack zeplin = TechStack.builder()
                .stackName("zeplin")
                .fields(design)
                .build();

        TechStack react = TechStack.builder()
                .stackName("react")
                .fields(front)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        postRepository.save(post);

        Post post2 = Post.builder()
                .title("title")
                .recruitType(PROJECT)
                .contact("010.0000.0000")
                .recruitIntroduction("???????????????")
                .expectedPeriod("1??????")
                .member(savedMember)
                .isCompleted(false)
                .build();

        Fields back2 = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(HIGH)
                .post(post2)
                .build();

        TechStack spring2 = TechStack.builder()
                .stackName("spring")
                .fields(back2)
                .build();

        postRepository.save(post2);

        // ?????? ??????
        mockMvc.perform(get("/api/search")
                        .param("recruitType", "total")
                        .param("keyword", "spring")
                        .param("page", "1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(2))
                .andExpect(jsonPath("$.content.[0].id").value(post2.getId()))
                .andDo(print());

        // ???????????? ??????
        mockMvc.perform(get("/api/search")
                        .param("recruitType", "study")
                        .param("keyword", "spring")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(1))
                .andDo(print());

        // ??????????????? ??????
        mockMvc.perform(get("/api/search")
                        .param("recruitType", "project")
                        .param("keyword", "spring")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(1))
                .andDo(print());

        // ????????? ??????
        mockMvc.perform(get("/api/search")
                        .param("recruitType", "total")
                        .param("keyword", "?????????")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(1))
                .andDo(print());

        // ?????? ?????? ??????
        mockMvc.perform(get("/api/search")
                        .param("recruitType", "total")
                        .param("keyword", "zeplin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()").value(1))
                .andDo(print());
    }

    @DisplayName("?????? ??? ?????? ??????.")
    @Test
    void updateTest() throws Exception {
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
                .title("??????")
                .recruitType(STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("?????????????????? ???????????????.")
                .isCompleted(false)
                .member(savedMember)
                .expectedPeriod("1??????")
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

        postRepository.save(post);

        UpdateTechStackRequest node = UpdateTechStackRequest.builder()
                .stackName("node.js")
                .build();

        UpdateTechStackRequest mysql = UpdateTechStackRequest.builder()
                .stackName("mysql")
                .build();

        UpdateTechStackRequest react = UpdateTechStackRequest.builder()
                .stackName("react")
                .build();

        UpdateFieldsRequest newField = UpdateFieldsRequest.builder()
                .fieldsName("???????????????")
                .recruitCount(50)
                .totalAbility("High")
                .stacks(new ArrayList<>())
                .isDelete(false)
                .build();

        newField.getStacks().add(react);

        UpdateFieldsRequest updateForBack = UpdateFieldsRequest.builder()
                .id(back.getId())
                .fieldsName("???????????? ??? ????????? ??????")
                .recruitCount(3)
                .totalAbility("Mid")
                .stacks(new ArrayList<>())
                .isDelete(false)
                .build();

        updateForBack.getStacks().add(node);
        updateForBack.getStacks().add(mysql);

        UpdatePostRequest updateForPost = UpdatePostRequest.builder()
                .title("titleeeee")
                .recruitType("project")
                .contact("email")
                .recruitIntroduction("Study nono Project gogo")
                .expectedPeriod("300??????")
                .fieldsList(new ArrayList<>())
                .build();

        updateForPost.getFieldsList().add(newField);
        updateForPost.getFieldsList().add(updateForBack);

        mockMvc.perform(put("/api/post/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateForPost))
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post.getId()))
                .andDo(print());

        assertThat(post.getTitle()).isEqualTo("titleeeee");
        assertThat(post.getRecruitType()).isEqualTo(PROJECT);
        assertThat(post.getContact()).isEqualTo("email");
        assertThat(post.getRecruitIntroduction()).isEqualTo("Study nono Project gogo");
        assertThat(post.getExpectedPeriod()).isEqualTo("300??????");
        assertThat(back.getFieldsName()).isEqualTo("???????????? ??? ????????? ??????");
        assertThat(back.getRecruitCount()).isEqualTo(3);
        assertThat(back.getTotalAbility()).isEqualTo(MID);
        assertThat(back.getStacks().get(0).getStackName()).isEqualTo("node.js");
        assertThat(back.getStacks().get(1).getStackName()).isEqualTo("mysql");
    }

    @DisplayName("?????? ?????? ??? ????????? ????????? ??? ??????, ???????????? ??? ??????(?????? ??????) ??????.")
    @Test
    void updateTest2() throws Exception {
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
                .title("??????")
                .recruitType(STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("????????? ????????? ???????????????.")
                .expectedPeriod("1??????")
                .isCompleted(false)
                .member(savedMember)
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

        Fields design = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(LOW)
                .post(post)
                .build();

        TechStack figma = TechStack.builder()
                .stackName("figma")
                .fields(design)
                .build();

        postRepository.save(post);

        UpdateTechStackRequest node = UpdateTechStackRequest.builder()
                .stackName("node.js")
                .build();

        UpdateTechStackRequest mysql = UpdateTechStackRequest.builder()
                .stackName("mysql")
                .build();

        UpdateTechStackRequest react = UpdateTechStackRequest.builder()
                .stackName("react")
                .build();

        UpdateTechStackRequest zeplin = UpdateTechStackRequest.builder()
                .stackName("zeplin")
                .build();

        UpdateFieldsRequest newField = UpdateFieldsRequest.builder()
                .fieldsName("???????????????")
                .recruitCount(50)
                .totalAbility("High")
                .stacks(new ArrayList<>())
                .isDelete(false)
                .build();

        newField.getStacks().add(react);

        UpdateFieldsRequest updateField = UpdateFieldsRequest.builder()
                .id(design.getId())
                .fieldsName("?????????")
                .recruitCount(50)
                .totalAbility("High")
                .stacks(new ArrayList<>())
                .isDelete(false)
                .build();

        updateField.getStacks().add(zeplin);

        UpdateFieldsRequest updateForBack = UpdateFieldsRequest.builder()
                .id(back.getId())
                .fieldsName("????????? ?????? ???????????? ????????? ?????????.")
                .recruitCount(3)
                .totalAbility("Low")
                .stacks(new ArrayList<>())
                .isDelete(true)
                .build();

        updateForBack.getStacks().add(node);
        updateForBack.getStacks().add(mysql);

        UpdatePostRequest updateForPost = UpdatePostRequest.builder()
                .title("titleeeee")
                .recruitType("project")
                .contact("email")
                .recruitIntroduction("Study nono Project gogo")
                .expectedPeriod("300??????")
                .fieldsList(new ArrayList<>())
                .build();

        updateForPost.getFieldsList().add(newField);
        updateForPost.getFieldsList().add(updateField);
        updateForPost.getFieldsList().add(updateForBack);

        mockMvc.perform(put("/api/post/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateForPost))
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post.getId()))
                .andDo(print());

        assertThat(fieldsRepository.findAll().size()).isEqualTo(2);
        assertThat(techStackRepository.findAll().size()).isEqualTo(2);
    }

    @DisplayName("?????? ?????? ????????? ????????????.")
    @Test
    void changeCompletedTest() throws Exception {
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
                .title("??????")
                .recruitType(STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("???????????????")
                .expectedPeriod("1??????")
                .isCompleted(false)
                .member(savedMember)
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

        Post savedPost = postRepository.save(post);

        IsCompletedDto isCompletedDto = new IsCompletedDto(true);

        mockMvc.perform(patch("/api/post/{id}", savedPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(isCompletedDto))
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post.getId()))
                .andDo(print());

        assertThat(post.getIsCompleted()).isTrue();
    }

    @DisplayName("?????? ????????????.")
    @Test
    void deletePostTest() throws Exception {
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
                .expectedPeriod("3000???")
                .isCompleted(false)
                .member(member)
                .build();

        Fields design = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(3)
                .totalAbility(LOW)
                .post(post)
                .build();

        Fields front = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(LOW)
                .post(post)
                .build();

        Fields back = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(LOW)
                .post(post)
                .build();

        TechStack zeplin = TechStack.builder()
                .stackName("zeplin")
                .fields(design)
                .build();

        TechStack react = TechStack.builder()
                .stackName("react")
                .fields(front)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        memberRepository.save(member);
        Post savedPost = postRepository.save(post);

        mockMvc.perform(delete("/api/post/{id}", savedPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andDo(print());

        assertThat(member.getPostList()).isEmpty();
        assertThat(postRepository.findAll()).isEmpty();
        assertThat(fieldsRepository.findAll()).isEmpty();
        assertThat(techStackRepository.findAll()).isEmpty();
    }

    @DisplayName("????????? ???????????? ????????????.")
    @Test
    void validationTest() throws Exception {

        CreateFieldsRequest forValidation = CreateFieldsRequest.builder()
                .fieldsName("")
                .stacks(new ArrayList<>())
                .build();

        CreatePostRequest post = CreatePostRequest.builder()
                .title("")
                .recruitType("")
                .contact("")
                .recruitIntroduction("")
                .expectedPeriod("")
                .fieldsList(new ArrayList<>())
                .build();

        post.getFieldsList().add(forValidation);

        Member member = Member.builder()
                .authId("savedAuthId")
                .picture("savedPicture")
                .name("savedName")
                .email("savedEmail")
                .refreshToken("savedRefreshToken")
                .roleType(RoleType.USER)
                .build();

        memberRepository.save(member);

        mockMvc.perform(post("/api/recruitment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post))
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("FIELDS-ERR-400"))
                .andExpect(jsonPath("$.errors.size()").value(9))
                .andDo(print());

    }

    @DisplayName("?????? ??? ?????? ??? PostNotExistException??? ????????????.")
    @Test
    void exceptionTest() throws Exception {
        mockMvc.perform(get("/api/post/{id}", Integer.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("???????????? ?????? ????????????."))
                .andExpect(jsonPath("$.code").value("POST-ERR-404"))
                .andDo(print());
    }
}















