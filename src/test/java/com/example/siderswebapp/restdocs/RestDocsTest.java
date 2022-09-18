package com.example.siderswebapp.restdocs;

import com.example.siderswebapp.domain.RecruitType;
import com.example.siderswebapp.domain.fields.Fields;
import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.domain.tech_stack.TechStack;
import com.example.siderswebapp.repository.post.PostRepository;
import com.example.siderswebapp.web.request.create.CreateFieldsRequest;
import com.example.siderswebapp.web.request.create.CreatePostRequest;
import com.example.siderswebapp.web.request.create.CreatedTechStackRequest;
import com.example.siderswebapp.web.request.update.UpdateFieldsRequest;
import com.example.siderswebapp.web.request.update.UpdatePostRequest;
import com.example.siderswebapp.web.request.update.UpdateTechStackRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private ObjectMapper objectMapper;

    @DisplayName("공통 요청 데이터 정보")
    @Test
    void commonRequestTest() throws Exception {
        List<CreatedTechStackRequest> backendStack = IntStream.range(1, 4)
                .mapToObj(i -> new CreatedTechStackRequest("백엔드스택" + i))
                .collect(Collectors.toList());

        CreateFieldsRequest backend = CreateFieldsRequest.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(9)
                .stacks(new ArrayList<>())
                .build();

        CreatePostRequest post = CreatePostRequest.builder()
                .title("제목")
                .recruitType("스터디")
                .contact("010.0000.0000")
                .recruitIntroduction("공부할 사람을 모집합니다.")
                .fieldsList(new ArrayList<>())
                .build();

        post.getFieldsList().add(backend);

        backend.getStacks().addAll(backendStack);

        mockMvc.perform(post("/recruitment")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andDo(document("commonRequest",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").type(STRING).description("모집글 제목"),
                                fieldWithPath("recruitType").type(STRING).description("모집 구분"),
                                fieldWithPath("contact").type(STRING).description("연락처"),
                                fieldWithPath("recruitIntroduction").type(STRING).description("모집글 내용"),
                                fieldWithPath("fieldsList[].fieldsName").type(STRING).description("모집 분야 구분"),
                                fieldWithPath("fieldsList[].recruitCount").type(NUMBER).description("모집 분야 별 모집 인원"),
                                fieldWithPath("fieldsList[].totalAbility").type(NUMBER).description("모집 분야 별 요구 능력치"),
                                fieldWithPath("fieldsList[].stacks[].stackName").type(STRING).description("기술 스택 이름")
                        )
                ));
    }

    @DisplayName("공통 응답 데이터 정보")
    @Test
    void commonResponseTest() throws Exception {
        Post post = Post.builder()
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할 사람을 모집합니다.")
                .isCompleted(false)
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(2)
                .post(post)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        Post savedPost = postRepository.save(post);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/post/{id}", savedPost.getId())
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("commonResponse",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("id").type(NUMBER).description("모집글 ID"),
                                fieldWithPath("title").type(STRING).description("모집글 제목"),
                                fieldWithPath("recruitType").type(STRING).description("모집 구분"),
                                fieldWithPath("contact").type(STRING).description("연락처"),
                                fieldWithPath("recruitIntroduction").type(STRING).description("모집글 내용"),
                                fieldWithPath("fieldsList[].id").type(NUMBER).description("모집 분야 ID"),
                                fieldWithPath("fieldsList[].fieldsName").type(STRING).description("모집 분야 구분"),
                                fieldWithPath("fieldsList[].recruitCount").type(NUMBER).description("모집 분야 별 모집 인원"),
                                fieldWithPath("fieldsList[].totalAbility").type(NUMBER).description("모집 분야 별 요구 능력치"),
                                fieldWithPath("fieldsList[].stacks[].id").type(NUMBER).description("기술 스택 ID"),
                                fieldWithPath("fieldsList[].stacks[].stackName").type(STRING).description("기술 스택 이름")
                        )

                ));
    }

    @DisplayName("여러건 조회 + 페이징 문서화")
    @Test
    void indexTest() throws Exception {
        Post post = Post.builder()
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할 사람을 모집합니다.")
                .isCompleted(false)
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(2)
                .post(post)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        Post savedPost = postRepository.save(post);

        mockMvc.perform(get("/?page=1&size=10")
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

    @DisplayName("단건 조회 문서화")
    @Test
    void readTest() throws Exception {
        Post post = Post.builder()
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할 사람을 모집합니다.")
                .isCompleted(false)
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(2)
                .post(post)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        Post savedPost = postRepository.save(post);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/post/{id}", savedPost.getId())
                        .accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("readPost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("모집글 ID")
                        )
                ));
    }

    @DisplayName("글 저장 문서화")
    @Test
    void saveTest() throws Exception {
        List<CreatedTechStackRequest> backendStack = IntStream.range(1, 4)
                .mapToObj(i -> new CreatedTechStackRequest("백엔드스택" + i))
                .collect(Collectors.toList());

        CreateFieldsRequest backend = CreateFieldsRequest.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(9)
                .stacks(new ArrayList<>())
                .build();

        CreatePostRequest post = CreatePostRequest.builder()
                .title("제목")
                .recruitType("스터디")
                .contact("010.0000.0000")
                .recruitIntroduction("공부할 사람을 모집합니다.")
                .fieldsList(new ArrayList<>())
                .build();

        post.getFieldsList().add(backend);

        backend.getStacks().addAll(backendStack);

        mockMvc.perform(post("/recruitment")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andDo(document("recruitment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())
                        ));
    }

    @DisplayName("글 수정 문서화")
    @Test
    void updateTest() throws Exception {
        Post post = Post.builder()
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할 사람을 모집합니다.")
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(2)
                .post(post)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        Fields design = Fields.builder()
                .fieldsName("디자인")
                .recruitCount(1)
                .totalAbility(2)
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
                .totalAbility(10)
                .stacks(new ArrayList<>())
                .isDelete(false)
                .build();

        newField.getStacks().add(react);

        UpdateFieldsRequest updateField = UpdateFieldsRequest.builder()
                .id(design.getId())
                .fieldsName("디자인")
                .recruitCount(50)
                .totalAbility(10)
                .stacks(new ArrayList<>())
                .isDelete(false)
                .build();

        updateField.getStacks().add(zeplin);

        UpdateFieldsRequest updateForBack = UpdateFieldsRequest.builder()
                .id(back.getId())
                .fieldsName("삭제가 되어 나타나지 않아야 합니다.")
                .recruitCount(3)
                .totalAbility(5)
                .stacks(new ArrayList<>())
                .isDelete(true)
                .build();

        updateForBack.getStacks().add(node);
        updateForBack.getStacks().add(mysql);

        UpdatePostRequest updateForPost = UpdatePostRequest.builder()
                .title("titleeeee")
                .recruitType("프로젝트")
                .contact("email")
                .recruitIntroduction("Study nono Project gogo")
                .fieldsList(new ArrayList<>())
                .build();

        updateForPost.getFieldsList().add(newField);
        updateForPost.getFieldsList().add(updateField);
        updateForPost.getFieldsList().add(updateForBack);

        mockMvc.perform(RestDocumentationRequestBuilders.patch("/post/{id}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateForPost)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("updatePost",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        relaxedRequestFields(
                                fieldWithPath("fieldsList[].id").type(NUMBER).description("수정할 필드의 ID (필드 새로 추가 시 null)").optional(),
                                fieldWithPath("fieldsList[].isDelete").type(BOOLEAN).description("true면 삭제O | false면 삭제X")
                        )
                ));
    }

    @DisplayName("글 삭제 문서화")
    @Test
    void deleteTest() throws Exception {
        Post post = Post.builder()
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할 사람을 모집합니다.")
                .isCompleted(false)
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(2)
                .post(post)
                .build();

        TechStack spring = TechStack.builder()
                .stackName("spring")
                .fields(back)
                .build();

        Post savedPost = postRepository.save(post);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/post/{id}", savedPost.getId())
                        .accept(APPLICATION_JSON))
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

        mockMvc.perform(post("/recruitment")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isBadRequest())
                .andDo(document("validation",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("title").description("제목은 빈 값이거나 null이면 안됩니다."),
                                fieldWithPath("recruitType").description("모집 구분을 반드시 선택해야 합니다.(스터디 || 프로젝트)"),
                                fieldWithPath("contact").description("연락처는 빈 값이거나 null이면 안됩니다."),
                                fieldWithPath("recruitIntroduction").description("글 내용은 빈 값이거나 null이면 안됩니다."),
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
        mockMvc.perform(get("/post/{id}", Integer.MAX_VALUE)
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
}