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

// TODO: 테스트 코드 1000줄 돌파할거같은거 에바인듯... 공통 요소좀 줄여보자..

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

    @DisplayName("공통 요청 데이터 정보")
    @Test
    void commonRequestTest() throws Exception {
        List<CreateTechStackRequest> backendStack = IntStream.range(1, 4)
                .mapToObj(i -> new CreateTechStackRequest("백엔드스택" + i))
                .collect(Collectors.toList());

        CreateFieldsRequest backend = CreateFieldsRequest.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility("Low")
                .stacks(new ArrayList<>())
                .build();

        CreatePostRequest post = CreatePostRequest.builder()
                .title("제목")
                .recruitType("study")
                .contact("010.0000.0000")
                .recruitIntroduction("공부할 사람을 모집합니다.")
                .expectedPeriod("1개월")
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
                                fieldWithPath("title").type(STRING).description("모집글 제목"),
                                fieldWithPath("recruitType").type(STRING).description("모집 구분 - 스터디 or 프로젝트"),
                                fieldWithPath("contact").type(STRING).description("연락처"),
                                fieldWithPath("recruitIntroduction").type(STRING).description("모집글 내용"),
                                fieldWithPath("expectedPeriod").type(STRING).description("예상 소요 기간"),
                                fieldWithPath("fieldsList[].fieldsName").type(STRING).description("모집 분야 구분"),
                                fieldWithPath("fieldsList[].recruitCount").type(NUMBER).description("모집 분야 별 모집 인원"),
                                fieldWithPath("fieldsList[].totalAbility").type(STRING).description("모집 분야 별 요구 능력치"),
                                fieldWithPath("fieldsList[].stacks[].stackName").type(STRING).description("기술 스택 이름")
                        )
                ));
    }

    @DisplayName("여러건 조회 + 페이징 문서화")
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
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할 사람을 모집합니다.")
                .expectedPeriod("1개월")
                .isCompleted(false)
                .member(savedMember)
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
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
                                parameterWithName("page").description("페이지 파라미터"),
                                parameterWithName("size").description("페이지 사이즈 파라미터")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("content[].id").type(NUMBER).description("모집글 ID"),
                                fieldWithPath("content[].title").type(STRING).description("모집글 제목"),
                                fieldWithPath("content[].recruitType").type(STRING).description("모집 구분"),
                                fieldWithPath("content[].createdDate").type(STRING).description("모집글 최초 작성 시간"),
                                fieldWithPath("content[].modifiedDate").type(STRING).description("모집글 마지막 수정 시간"),
                                fieldWithPath("content[].fieldsList[].id").type(NUMBER).description("모집 분야 ID"),
                                fieldWithPath("content[].fieldsList[].fieldsName").type(STRING).description("모집 분야 구분"),
                                fieldWithPath("content[].fieldsList[].recruitCount").type(NUMBER).description("모집 분야 별 모집 인원"),
                                fieldWithPath("content[].fieldsList[].stacks[].id").type(NUMBER).description("기술 스택 ID"),
                                fieldWithPath("content[].fieldsList[].stacks[].stackName").type(STRING).description("기술 스택 이름"),
                                fieldWithPath("pageable").type(OBJECT).description("페이징 정보"),
                                fieldWithPath("pageable.pageNumber").type(NUMBER).description("현재 페이지 번호(page=0과 page=1은 서로 동일)"),
                                fieldWithPath("pageable.offset").type(NUMBER).description("페이지의 시작 글 번호"),
                                fieldWithPath("totalPages").type(NUMBER).description("전체 페이지 수"),
                                fieldWithPath("totalElements").type(NUMBER).description("전체 글 개수"),
                                fieldWithPath("size").type(NUMBER).description("페이지 사이즈(기본값=10)"),
                                fieldWithPath("numberOfElements").type(NUMBER).description("요청 페이지에서 조회된 데이터의 개수"),
                                fieldWithPath("first").type(BOOLEAN).description("첫 페이지 여부")
                        )
                ));
    }

    @DisplayName("글 검색 문서화")
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
                .title("제목")
                .recruitType(STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할사람")
                .expectedPeriod("1개월")
                .member(savedMember)
                .isCompleted(false)
                .build();

        Fields design = Fields.builder()
                .fieldsName("디자인")
                .recruitCount(3)
                .totalAbility(LOW)
                .post(post)
                .build();

        Fields front = Fields.builder()
                .fieldsName("프론트")
                .recruitCount(1)
                .totalAbility(MID)
                .post(post)
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
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
                .recruitIntroduction("공부할사람")
                .expectedPeriod("1개월")
                .member(savedMember)
                .isCompleted(false)
                .build();

        Fields back2 = Fields.builder()
                .fieldsName("백엔드")
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
                                parameterWithName("recruitType").description("모집 분야 : total - 전체 | study - 스터디 | project - 프로젝트"),
                                parameterWithName("keyword").description("검색 키워드 : 현재 단어만 검색 가능합니다. | target : 글 제목, 글 내용, 필드 이름, 기술 스택 이름")
                        )
                ));
    }

    @DisplayName("단건 조회 문서화")
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
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할 사람을 모집합니다.")
                .expectedPeriod("1개월")
                .member(savedMember)
                .isCompleted(false)
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
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
                                parameterWithName("id").description("모집글 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("모집글 ID"),
                                fieldWithPath("title").type(STRING).description("모집글 제목"),
                                fieldWithPath("recruitType").type(STRING).description("모집 구분"),
                                fieldWithPath("contact").type(STRING).description("연락처"),
                                fieldWithPath("recruitIntroduction").type(STRING).description("모집글 내용"),
                                fieldWithPath("expectedPeriod").type(STRING).description("예상 소요 기간"),
                                fieldWithPath("authId").type(STRING).description("글을 저장한 유저의 아이디"),
                                fieldWithPath("isCompleted").type(BOOLEAN).description("모집 완료 여부 - true:완료, false:미완료"),
                                fieldWithPath("isWriter").type(BOOLEAN).description("글 작성자 여부 - true:작성자, false:타인"),
                                fieldWithPath("createdDate").type(STRING).description("모집글 최초 작성 시간"),
                                fieldWithPath("modifiedDate").type(STRING).description("모집글 마지막 수정 시간"),
                                fieldWithPath("fieldsList[].id").type(NUMBER).description("모집 분야 ID"),
                                fieldWithPath("fieldsList[].fieldsName").type(STRING).description("모집 분야 구분"),
                                fieldWithPath("fieldsList[].recruitCount").type(NUMBER).description("모집 분야 별 모집 인원"),
                                fieldWithPath("fieldsList[].totalAbility").type(STRING).description("모집 분야 별 요구 능력치"),
                                fieldWithPath("fieldsList[].stacks[].id").type(NUMBER).description("기술 스택 ID"),
                                fieldWithPath("fieldsList[].stacks[].stackName").type(STRING).description("기술 스택 이름")
                        )
                ));
    }

    @DisplayName("글 저장 문서화")
    @Test
    void saveTest() throws Exception {
        List<CreateTechStackRequest> backendStack = IntStream.range(1, 4)
                .mapToObj(i -> new CreateTechStackRequest("백엔드스택" + i))
                .collect(Collectors.toList());

        CreateFieldsRequest backend = CreateFieldsRequest.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility("Low")
                .stacks(new ArrayList<>())
                .build();

        CreatePostRequest post = CreatePostRequest.builder()
                .title("제목")
                .recruitType("study")
                .contact("010.0000.0000")
                .recruitIntroduction("공부할 사람을 모집합니다.")
                .expectedPeriod("1개월")
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
                                fieldWithPath("postId").type(NUMBER).description("저장된 글의 ID")
                        )
                        ));
    }

    @DisplayName("글 수정 문서화")
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

        // 이미 저장이 된 상태라, savedMember를 사용할 필요는 없을 것 같다.
        Member savedMember = memberRepository.save(member);

        Post post = Post.builder()
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할 사람을 모집합니다.")
                .isCompleted(false)
                .member(savedMember)
                .expectedPeriod("1개월")
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(Ability.LOW)
                .post(post)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        Fields design = Fields.builder()
                .fieldsName("디자인")
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
                .fieldsName("프론트엔드")
                .recruitCount(50)
                .totalAbility("High")
                .stacks(new ArrayList<>())
                .isDelete(false)
                .build();

        newField.getStacks().add(react);

        UpdateFieldsRequest updateField = UpdateFieldsRequest.builder()
                .id(design.getId())
                .fieldsName("디자인")
                .recruitCount(50)
                .totalAbility("High")
                .stacks(new ArrayList<>())
                .isDelete(false)
                .build();

        updateField.getStacks().add(zeplin);

        UpdateFieldsRequest updateForBack = UpdateFieldsRequest.builder()
                .id(back.getId())
                .fieldsName("삭제가 되어 나타나지 않아야 합니다.")
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
                .expectedPeriod("3개월")
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
                                parameterWithName("id").description("모집글 ID")
                        ),
                        relaxedRequestFields(
                                fieldWithPath("fieldsList[].id").type(NUMBER).description("수정할 필드의 ID (필드 새로 추가 시 null)").optional(),
                                fieldWithPath("fieldsList[].isDelete").type(BOOLEAN).description("true면 삭제O | false면 삭제X")
                        ),
                        responseFields(
                                fieldWithPath("postId").type(NUMBER).description("저장된 글의 ID")
                        )
                ));
    }

    @DisplayName("모집 완료 여부 변경 문서화")
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

        // 이미 저장이 된 상태라, savedMember를 사용할 필요는 없을 것 같다.
        Member savedMember = memberRepository.save(member);

        Post post = Post.builder()
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할 사람을 모집합니다.")
                .expectedPeriod("1개월")
                .isCompleted(false)
                .member(savedMember)
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
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
                                parameterWithName("id").description("모집글 ID")
                        ),
                        requestFields(
                                fieldWithPath("isCompleted").type(BOOLEAN).description("true면 모집 마감 | false면 모집 중")
                        ),
                        responseFields(
                                fieldWithPath("postId").type(NUMBER).description("수정한 글의 ID")
                        )
                ));
    }

    @DisplayName("글 삭제 문서화")
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

        // 이미 저장이 된 상태라, savedMember를 사용할 필요는 없을 것 같다.
        Member savedMember = memberRepository.save(member);

        Post post = Post.builder()
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할 사람을 모집합니다.")
                .isCompleted(false)
                .member(savedMember)
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
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
                                parameterWithName("id").description("모집글 ID")
                        )
                ));
    }

    @DisplayName("검증 문서화")
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
                                fieldWithPath("title").description("제목은 빈 값이거나 null이면 안됩니다."),
                                fieldWithPath("recruitType").description("모집 구분을 반드시 선택해야 합니다.(스터디 || 프로젝트)"),
                                fieldWithPath("contact").description("연락처는 빈 값이거나 null이면 안됩니다."),
                                fieldWithPath("recruitIntroduction").description("글 내용은 빈 값이거나 null이면 안됩니다."),
                                fieldWithPath("expectedPeriod").description("예상 소요 기간은 반드시 선택해야 합니다."),
                                fieldWithPath("fieldsList").description("모집 분야는 1개 이상 선택해야 합니다."),
                                fieldWithPath("fieldsList[].fieldsName").description("필드명은 빈 값이거나 null이면 안됩니다."),
                                fieldWithPath("fieldsList[].recruitCount").description("모집 인원은 null이면 안됩니다."),
                                fieldWithPath("fieldsList[].totalAbility").description("요구 능력치는 null이면 안됩니다."),
                                fieldWithPath("fieldsList[].stacks[]").description("기술 스택은 1개 이상 선택해야 합니다.")
                        ),
                        responseFields(
                                fieldWithPath("status").type(NUMBER).description("응답 상태 값"),
                                fieldWithPath("code").type(STRING).description("에러 코드"),
                                fieldWithPath("errors").type(ARRAY).description("에러가 발생한 필드 리스트"),
                                fieldWithPath("errors[].fieldsName").type(STRING).description("에러가 발생한 필드 이름"),
                                fieldWithPath("errors[].message").type(STRING).description("에러가 발생한 필드의 검증 메세지")
                        )
                ));

    }

    @DisplayName("예외 문서화")
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
                                fieldWithPath("message").type(STRING).description("에러 메세지"),
                                fieldWithPath("code").type(STRING).description("에러 코드")
                        )
                ));
    }

    @DisplayName("회원 가입 문서화")
    @Test
    void signUp() throws Exception {
        SignUpDto signUpDto = new SignUpDto("유저닉네임");

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
                                fieldWithPath("name").type(STRING).description("유저 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("authId").type(STRING).description("authId - 유저 고유 번호"),
                                fieldWithPath("name").type(STRING).description("유저 닉네임"),
                                fieldWithPath("picture").type(STRING).description("유저 프로필 사진"),
                                fieldWithPath("accessToken").type(STRING).description("accessToken")
                                )
                ));
    }

    @DisplayName("회원 정보 조회")
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
                                fieldWithPath("authId").type(STRING).description("authId - 유저 고유 번호"),
                                fieldWithPath("name").type(STRING).description("유저 닉네임"),
                                fieldWithPath("picture").type(STRING).description("유저 프로필 사진"),
                                fieldWithPath("isAuthMember").type(BOOLEAN).description("인증 유저 여부 : 인증 유저 - true | 미인증 유저 - false")
                        )
                ));
    }

    @DisplayName("닉네임 중복 검사 문서화")
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
                                parameterWithName("name").description("유저가 가입 시 작성한 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("isExists").type(BOOLEAN).description("닉네임 중복 여부 : 중복됨 - true | 중복 안됨 - false")
                        )
                ));
    }

    @DisplayName("회원 탈퇴 문서화")
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
                .andDo(document("deleteMember"));
    }
}
