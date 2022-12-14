package com.example.siderswebapp.restdocs;

import static com.example.siderswebapp.TestAttributes.TEST_ATTRIBUTES;
import static com.example.siderswebapp.post.domain.Ability.HIGH;
import static com.example.siderswebapp.post.domain.Ability.LOW;
import static com.example.siderswebapp.post.domain.Ability.MID;
import static com.example.siderswebapp.post.domain.RecruitType.PROJECT;
import static com.example.siderswebapp.post.domain.RecruitType.STUDY;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.OBJECT;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.siderswebapp.member.application.dto.SignUpDto;
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
import com.example.siderswebapp.post.domain.Ability;
import com.example.siderswebapp.post.domain.Fields;
import com.example.siderswebapp.post.domain.Post;
import com.example.siderswebapp.post.domain.RecruitType;
import com.example.siderswebapp.post.domain.TechStack;
import com.example.siderswebapp.post.domain.repository.post.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

// TODO: ????????? ?????? 1000??? ????????????????????? ????????????... ?????? ????????? ????????????..

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https", uriHost = "api.siders.com", uriPort = 443)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class RestDocsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("?????? ?????? ????????? ??????")
    @Test
    void commonRequestTest() throws Exception {
        List<CreateTechStackRequest> backendStack = IntStream.range(1, 4)
                .mapToObj(i -> new CreateTechStackRequest("???????????????" + i))
                .collect(Collectors.toList());

        CreateFieldsRequest backend = CreateFieldsRequest.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility("Low")
                .stacks(new ArrayList<>())
                .build();

        CreatePostRequest post = CreatePostRequest.builder()
                .title("??????")
                .recruitType("study")
                .contact("010.0000.0000")
                .recruitIntroduction("????????? ????????? ???????????????.")
                .expectedPeriod("1??????")
                .fieldsList(new ArrayList<>())
                .build();

        post.getFieldsList().add(backend);

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
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post))
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andDo(document("commonRequest",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(STRING).description("????????? ??????"),
                                fieldWithPath("recruitType").type(STRING).description("?????? ?????? - ????????? or ????????????"),
                                fieldWithPath("contact").type(STRING).description("?????????"),
                                fieldWithPath("recruitIntroduction").type(STRING).description("????????? ??????"),
                                fieldWithPath("expectedPeriod").type(STRING).description("?????? ?????? ??????"),
                                fieldWithPath("fieldsList[].fieldsName").type(STRING).description("?????? ?????? ??????"),
                                fieldWithPath("fieldsList[].recruitCount").type(NUMBER).description("?????? ?????? ??? ?????? ??????"),
                                fieldWithPath("fieldsList[].totalAbility").type(STRING).description("?????? ?????? ??? ?????? ?????????"),
                                fieldWithPath("fieldsList[].stacks[].stackName").type(STRING).description("?????? ?????? ??????")
                        )
                ));
    }

    @DisplayName("????????? ?????? + ????????? ?????????")
    @Test
    void indexTest() throws Exception {
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
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("????????? ????????? ???????????????.")
                .expectedPeriod("1??????")
                .isCompleted(false)
                .member(savedMember)
                .build();

        Fields back = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(Ability.LOW)
                .post(post)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        postRepository.save(post);

        mockMvc.perform(get("/api/posts?page=1&size=10")
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("pagingPosts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("page").description("????????? ????????????"),
                                parameterWithName("size").description("????????? ????????? ????????????")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("content[].id").type(NUMBER).description("????????? ID"),
                                fieldWithPath("content[].title").type(STRING).description("????????? ??????"),
                                fieldWithPath("content[].recruitType").type(STRING).description("?????? ??????"),
                                fieldWithPath("content[].createdDate").type(STRING).description("????????? ?????? ?????? ??????"),
                                fieldWithPath("content[].modifiedDate").type(STRING).description("????????? ????????? ?????? ??????"),
                                fieldWithPath("content[].fieldsList[].id").type(NUMBER).description("?????? ?????? ID"),
                                fieldWithPath("content[].fieldsList[].fieldsName").type(STRING).description("?????? ?????? ??????"),
                                fieldWithPath("content[].fieldsList[].recruitCount").type(NUMBER).description("?????? ?????? ??? ?????? ??????"),
                                fieldWithPath("content[].fieldsList[].stacks[].id").type(NUMBER).description("?????? ?????? ID"),
                                fieldWithPath("content[].fieldsList[].stacks[].stackName").type(STRING).description("?????? ?????? ??????"),
                                fieldWithPath("pageable").type(OBJECT).description("????????? ??????"),
                                fieldWithPath("pageable.pageNumber").type(NUMBER).description("?????? ????????? ??????(page=0??? page=1??? ?????? ??????)"),
                                fieldWithPath("pageable.offset").type(NUMBER).description("???????????? ?????? ??? ??????"),
                                fieldWithPath("totalPages").type(NUMBER).description("?????? ????????? ???"),
                                fieldWithPath("totalElements").type(NUMBER).description("?????? ??? ??????"),
                                fieldWithPath("size").type(NUMBER).description("????????? ?????????(?????????=10)"),
                                fieldWithPath("numberOfElements").type(NUMBER).description("?????? ??????????????? ????????? ???????????? ??????"),
                                fieldWithPath("first").type(BOOLEAN).description("??? ????????? ??????")
                        )
                ));
    }

    @DisplayName("??? ?????? ?????????")
    @Test
    void searchPost() throws Exception {
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

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/search")
                        .param("recruitType", "study")
                        .param("keyword", "spring")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("postSearch",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("recruitType").description("?????? ?????? : total - ?????? | study - ????????? | project - ????????????"),
                                parameterWithName("keyword").description("?????? ????????? : ?????? ????????? ?????? ???????????????. | target : ??? ??????, ??? ??????, ?????? ??????, ?????? ?????? ??????")
                        )
                ));
    }

    @DisplayName("?????? ?????? ?????????")
    @Test
    void readTest() throws Exception {
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
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("????????? ????????? ???????????????.")
                .expectedPeriod("1??????")
                .member(savedMember)
                .isCompleted(false)
                .build();

        Fields back = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(Ability.LOW)
                .post(post)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        Post savedPost = postRepository.save(post);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/post/{id}", savedPost.getId())
                        .accept(APPLICATION_JSON)
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andDo(document("readPost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("????????? ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("????????? ID"),
                                fieldWithPath("title").type(STRING).description("????????? ??????"),
                                fieldWithPath("recruitType").type(STRING).description("?????? ??????"),
                                fieldWithPath("contact").type(STRING).description("?????????"),
                                fieldWithPath("recruitIntroduction").type(STRING).description("????????? ??????"),
                                fieldWithPath("expectedPeriod").type(STRING).description("?????? ?????? ??????"),
                                fieldWithPath("authId").type(STRING).description("?????? ????????? ????????? ?????????"),
                                fieldWithPath("isCompleted").type(BOOLEAN).description("?????? ?????? ?????? - true:??????, false:?????????"),
                                fieldWithPath("isWriter").type(BOOLEAN).description("??? ????????? ?????? - true:?????????, false:??????"),
                                fieldWithPath("createdDate").type(STRING).description("????????? ?????? ?????? ??????"),
                                fieldWithPath("modifiedDate").type(STRING).description("????????? ????????? ?????? ??????"),
                                fieldWithPath("fieldsList[].id").type(NUMBER).description("?????? ?????? ID"),
                                fieldWithPath("fieldsList[].fieldsName").type(STRING).description("?????? ?????? ??????"),
                                fieldWithPath("fieldsList[].recruitCount").type(NUMBER).description("?????? ?????? ??? ?????? ??????"),
                                fieldWithPath("fieldsList[].totalAbility").type(STRING).description("?????? ?????? ??? ?????? ?????????"),
                                fieldWithPath("fieldsList[].stacks[].id").type(NUMBER).description("?????? ?????? ID"),
                                fieldWithPath("fieldsList[].stacks[].stackName").type(STRING).description("?????? ?????? ??????")
                        )
                ));
    }

    @DisplayName("??? ?????? ?????????")
    @Test
    void saveTest() throws Exception {
        List<CreateTechStackRequest> backendStack = IntStream.range(1, 4)
                .mapToObj(i -> new CreateTechStackRequest("???????????????" + i))
                .collect(Collectors.toList());

        CreateFieldsRequest backend = CreateFieldsRequest.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility("Low")
                .stacks(new ArrayList<>())
                .build();

        CreatePostRequest post = CreatePostRequest.builder()
                .title("??????")
                .recruitType("study")
                .contact("010.0000.0000")
                .recruitIntroduction("????????? ????????? ???????????????.")
                .expectedPeriod("1??????")
                .fieldsList(new ArrayList<>())
                .build();

        post.getFieldsList().add(backend);

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
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post))
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andDo(document("recruitment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("postId").type(NUMBER).description("????????? ?????? ID")
                        )
                        ));
    }

    @DisplayName("??? ?????? ?????????")
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

        // ?????? ????????? ??? ?????????, savedMember??? ????????? ????????? ?????? ??? ??????.
        Member savedMember = memberRepository.save(member);

        Post post = Post.builder()
                .title("??????")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("????????? ????????? ???????????????.")
                .isCompleted(false)
                .member(savedMember)
                .expectedPeriod("1??????")
                .build();

        Fields back = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(Ability.LOW)
                .post(post)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        Fields design = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(Ability.LOW)
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
                .expectedPeriod("3??????")
                .fieldsList(new ArrayList<>())
                .build();

        updateForPost.getFieldsList().add(newField);
        updateForPost.getFieldsList().add(updateField);
        updateForPost.getFieldsList().add(updateForBack);

        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/post/{id}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateForPost))
                        .with(user("savedAuthId").password("").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("updatePost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("????????? ID")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("fieldsList[].id").type(NUMBER).description("????????? ????????? ID (?????? ?????? ?????? ??? null)").optional(),
                                fieldWithPath("fieldsList[].isDelete").type(BOOLEAN).description("true??? ??????O | false??? ??????X")
                        ),
                        responseFields(
                                fieldWithPath("postId").type(NUMBER).description("????????? ?????? ID")
                        )
                ));
    }

    @DisplayName("?????? ?????? ?????? ?????? ?????????")
    @Test
    void changeCompletionTest() throws Exception {
        Member member = Member.builder()
                .authId("savedAuthId")
                .picture("savedPicture")
                .name("savedName")
                .email("savedEmail")
                .refreshToken("savedRefreshToken")
                .roleType(RoleType.USER)
                .build();

        // ?????? ????????? ??? ?????????, savedMember??? ????????? ????????? ?????? ??? ??????.
        Member savedMember = memberRepository.save(member);

        Post post = Post.builder()
                .title("??????")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("????????? ????????? ???????????????.")
                .expectedPeriod("1??????")
                .isCompleted(false)
                .member(savedMember)
                .build();

        Fields back = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(Ability.LOW)
                .post(post)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        Post savedPost = postRepository.save(post);

        IsCompletedDto isCompletedDto = new IsCompletedDto(true);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/post/{id}", savedPost.getId())
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(isCompletedDto))
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andDo(document("changeCompletion",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("????????? ID")
                        ),
                        requestFields(
                                fieldWithPath("isCompleted").type(BOOLEAN).description("true??? ?????? ?????? | false??? ?????? ???")
                        ),
                        responseFields(
                                fieldWithPath("postId").type(NUMBER).description("????????? ?????? ID")
                        )
                ));
    }

    @DisplayName("??? ?????? ?????????")
    @Test
    void deleteTest() throws Exception {
        Member member = Member.builder()
                .authId("savedAuthId")
                .picture("savedPicture")
                .name("savedName")
                .email("savedEmail")
                .refreshToken("savedRefreshToken")
                .roleType(RoleType.USER)
                .build();

        // ?????? ????????? ??? ?????????, savedMember??? ????????? ????????? ?????? ??? ??????.
        Member savedMember = memberRepository.save(member);

        Post post = Post.builder()
                .title("??????")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("????????? ????????? ???????????????.")
                .isCompleted(false)
                .member(savedMember)
                .build();

        Fields back = Fields.builder()
                .fieldsName("?????????")
                .recruitCount(1)
                .totalAbility(Ability.LOW)
                .post(post)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        Post savedPost = postRepository.save(post);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/post/{id}", savedPost.getId())
                        .accept(APPLICATION_JSON)
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andDo(document("deletePost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("????????? ID")
                        )
                ));
    }

    @DisplayName("?????? ?????????")
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
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post))
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isBadRequest())
                .andDo(document("validation",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").description("????????? ??? ???????????? null?????? ????????????."),
                                fieldWithPath("recruitType").description("?????? ????????? ????????? ???????????? ?????????.(????????? || ????????????)"),
                                fieldWithPath("contact").description("???????????? ??? ???????????? null?????? ????????????."),
                                fieldWithPath("recruitIntroduction").description("??? ????????? ??? ???????????? null?????? ????????????."),
                                fieldWithPath("expectedPeriod").description("?????? ?????? ????????? ????????? ???????????? ?????????."),
                                fieldWithPath("fieldsList").description("?????? ????????? 1??? ?????? ???????????? ?????????."),
                                fieldWithPath("fieldsList[].fieldsName").description("???????????? ??? ???????????? null?????? ????????????."),
                                fieldWithPath("fieldsList[].recruitCount").description("?????? ????????? null?????? ????????????."),
                                fieldWithPath("fieldsList[].totalAbility").description("?????? ???????????? null?????? ????????????."),
                                fieldWithPath("fieldsList[].stacks[]").description("?????? ????????? 1??? ?????? ???????????? ?????????.")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("?????? ?????? ???"),
                                fieldWithPath("code").type(STRING).description("?????? ??????"),
                                fieldWithPath("errors").type(ARRAY).description("????????? ????????? ?????? ?????????"),
                                fieldWithPath("errors[].fieldsName").type(STRING).description("????????? ????????? ?????? ??????"),
                                fieldWithPath("errors[].message").type(STRING).description("????????? ????????? ????????? ?????? ?????????")
                        )
                ));

    }

    @DisplayName("?????? ?????????")
    @Test
    void exceptionTest() throws Exception {
        mockMvc.perform(get("/api/post/{id}", Integer.MAX_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(document("exception",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("Response status"),
                                fieldWithPath("message").type(STRING).description("?????? ?????????"),
                                fieldWithPath("code").type(STRING).description("?????? ??????")
                        )
                ));
    }

    @DisplayName("?????? ?????? ?????????")
    @Test
    void signUp() throws Exception {
        SignUpDto signUpDto = new SignUpDto("???????????????");

        mockMvc.perform(post("/api/signup")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpDto))
                        .with(user("authId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andDo(document("signup",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").type(STRING).description("?????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("authId").type(STRING).description("authId - ?????? ?????? ??????"),
                                fieldWithPath("name").type(STRING).description("?????? ?????????"),
                                fieldWithPath("picture").type(STRING).description("?????? ????????? ??????"),
                                fieldWithPath("accessToken").type(STRING).description("accessToken")
                                )
                ));
    }

    @DisplayName("?????? ?????? ??????")
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
                        .contentType(APPLICATION_JSON)
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andDo(document("memberInfo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("authId").type(STRING).description("authId - ?????? ?????? ??????"),
                                fieldWithPath("name").type(STRING).description("?????? ?????????"),
                                fieldWithPath("picture").type(STRING).description("?????? ????????? ??????"),
                                fieldWithPath("isAuthMember").type(BOOLEAN).description("?????? ?????? ?????? : ?????? ?????? - true | ????????? ?????? - false")
                        )
                ));
    }

    @DisplayName("????????? ?????? ?????? ?????????")
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

        Map<String, Object> attributes = TEST_ATTRIBUTES.getAttributes();

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/signup?name=savedName")
                        .contentType(APPLICATION_JSON)
                        .with(oauth2Login().attributes(attr -> attr.putAll(attributes))))
                .andExpect(status().isOk())
                .andDo(document("duplicateNameCheck",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("name").description("????????? ?????? ??? ????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("isExists").type(BOOLEAN).description("????????? ?????? ?????? : ????????? - true | ?????? ?????? - false")
                        )
                ));
    }

    @DisplayName("?????? ?????? ?????????")
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

        postRepository.save(post);

        mockMvc.perform(delete("/api/member")
                        .with(user("savedAuthId").password("").roles("USER")))
                .andExpect(status().isOk())
                .andDo(document("deleteMember"));
    }
}
