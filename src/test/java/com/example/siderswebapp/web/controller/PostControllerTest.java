package com.example.siderswebapp.web.controller;

import com.example.siderswebapp.domain.RecruitType;
import com.example.siderswebapp.domain.fields.Fields;
import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.domain.tech_stack.TechStack;
import com.example.siderswebapp.repository.fields.FieldsRepository;
import com.example.siderswebapp.repository.post.PostRepository;
import com.example.siderswebapp.repository.tech_stack.TechStackRepository;
import com.example.siderswebapp.web.request.create.CreateFieldsRequest;
import com.example.siderswebapp.web.request.create.CreatePostRequest;
import com.example.siderswebapp.web.request.create.CreatedTechStackRequest;
import com.example.siderswebapp.web.request.update.UpdateFieldsRequest;
import com.example.siderswebapp.web.request.update.UpdatePostRequest;
import com.example.siderswebapp.web.request.update.UpdateTechStackRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @DisplayName("글 작성 후, 정보들이 잘 저장된다.")
    @Test
    void recruitmentTest() throws Exception {
        List<CreatedTechStackRequest> designStack = IntStream.range(1, 4)
                .mapToObj(i -> new CreatedTechStackRequest("디자인스택" + i))
                .collect(Collectors.toList());

        List<CreatedTechStackRequest> frontendStack = IntStream.range(1, 4)
                .mapToObj(i -> new CreatedTechStackRequest("프론트엔드스택" + i))
                .collect(Collectors.toList());

        List<CreatedTechStackRequest> backendStack = IntStream.range(1, 4)
                .mapToObj(i -> new CreatedTechStackRequest("백엔드스택" + i))
                .collect(Collectors.toList());

        CreateFieldsRequest design = CreateFieldsRequest.builder()
                .fieldsName("디자인")
                .recruitCount(2)
                .totalAbility(4)
                .stacks(new ArrayList<>())
                .build();

        CreateFieldsRequest frontend = CreateFieldsRequest.builder()
                .fieldsName("프론트엔드")
                .recruitCount(3)
                .totalAbility(6)
                .stacks(new ArrayList<>())
                .build();

        CreateFieldsRequest backend = CreateFieldsRequest.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(9)
                .stacks(new ArrayList<>())
                .build();

        CreatePostRequest post = CreatePostRequest.builder()
                .title("제목")
                .recruitType("스터디")
                .contact("010-0000-1111")
                .recruitIntroduction("스터디 구하니까 오셈ㅋ")
                .fieldsList(new ArrayList<>())
                .build();

        post.getFieldsList().add(design);
        post.getFieldsList().add(frontend);
        post.getFieldsList().add(backend);

        design.getStacks().addAll(designStack);
        frontend.getStacks().addAll(frontendStack);
        backend.getStacks().addAll(backendStack);


        mockMvc.perform(post("/recruitment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.recruitType").value("STUDY"))
                .andExpect(jsonPath("$.contact").value("010-0000-1111"))
                .andExpect(jsonPath("$.recruitIntroduction").value("스터디 구하니까 오셈ㅋ"))
                .andExpect(jsonPath("$.fieldsList.[0].fieldsName").value("디자인"))
                .andExpect(jsonPath("$.fieldsList.[0].stacks.[0].stackName").value("디자인스택1"))
                .andDo(print());

        assertThat(postRepository.findAll().size()).isEqualTo(1);
        assertThat(fieldsRepository.findAll().size()).isEqualTo(3);
        assertThat(techStackRepository.findAll().size()).isEqualTo(9);
    }

    @DisplayName("글이 조회된다.")
    @Test
    void readPostTest() throws Exception {
        Post post = Post.builder()
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할사람")
                .isCompleted(false)
                .build();

        Fields design = Fields.builder()
                .fieldsName("디자인")
                .recruitCount(3)
                .totalAbility(6)
                .post(post)
                .build();

        Fields front = Fields.builder()
                .fieldsName("프론트")
                .recruitCount(1)
                .totalAbility(3)
                .post(post)
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(2)
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

        mockMvc.perform(get("/post/{id}", savedPost.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.recruitType").value("STUDY"))
                .andExpect(jsonPath("$.recruitIntroduction").value("공부할사람"))
                .andExpect(jsonPath("$.fieldsList.[0].fieldsName").value("디자인"))
                .andExpect(jsonPath("$.fieldsList.[0].stacks.[0].stackName").value("zeplin"))
                .andDo(print());
    }

    @DisplayName("페이징 테스트 - 모집이 완료되지 않은 글만 노출된다.")
    @Test
    void testPaging() throws Exception {
        List<Post> postList = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("title " + i)
                        .recruitType(RecruitType.STUDY)
                        .contact("email")
                        .recruitIntroduction("content " + i)
                        .isCompleted(false)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(postList);

        Post post = Post.builder()
                .isCompleted(true)
                .recruitType(RecruitType.PROJECT)
                .contact("333")
                .recruitIntroduction("intro")
                .title("title")
                .build();

        postRepository.save(post);

        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0].title").value("title 30"))
                .andExpect(jsonPath("$.content.[9].title").value("title 21"))
                .andDo(print());
    }

    @DisplayName("페이징 테스트 - 모집 완료 글만 있을 경우 전혀 노출되지 않는다.")
    @Test
    void testPaging2() throws Exception {
        List<Post> postList = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("title " + i)
                        .recruitType(RecruitType.STUDY)
                        .contact("email")
                        .recruitIntroduction("content " + i)
                        .isCompleted(true)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(postList);

        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[0]").doesNotExist())
                .andDo(print());

    }

    @DisplayName("글이 잘 수정 된다.")
    @Test
    void updateTest() throws Exception {
        Post post = Post.builder()
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할사람")
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

        // TODO:원래 있던 백엔드 필드를 프론트엔드로 변경..은 안되게 하자..
        // 분야, 스택 필드 삭제? 원래 필드가 값이 null로 바뀌면?
        // 영속성 전이때문에 완전 삭제는 위험함 (그냥 null이면 노출이 안되게 해야하는건지..)
        // 추가는 가능
        // 스택, 분야 삭제 보다는 빈값으로 바꾸는 것은 가능. 하지만 일단 validation을 notblank를 걸어놔서.. 하나라도 있어야된다. 로 바꾸면 안될까
        UpdateFieldsRequest newField = UpdateFieldsRequest.builder()
                .fieldsName("프론트엔드")
                .recruitCount(50)
                .totalAbility(10)
                .stacks(new ArrayList<>())
                .isDelete(false)
                .build();

        newField.getStacks().add(react);

        UpdateFieldsRequest updateForBack = UpdateFieldsRequest.builder()
                .id(back.getId())
                .fieldsName("기술 스택도 수정이 되어야 합니다.")
                .recruitCount(3)
                .totalAbility(5)
                .stacks(new ArrayList<>())
                .isDelete(false)
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
        updateForPost.getFieldsList().add(updateForBack);

        // TODO: JSON Path 확인 로직 작성.
        mockMvc.perform(patch("/post/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateForPost)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("글이 삭제 될 필드는 삭제가 잘 되고, 나머지는 잘 수정(혹은 추가) 된다.")
    @Test
    void updateTest2() throws Exception {
        Post post = Post.builder()
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할사람")
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

        // TODO: JSON Path 확인 로직 작성.
        mockMvc.perform(patch("/post/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateForPost)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("글이 삭제된다.")
    @Test
    void deletePostTest() throws Exception {
        Post post = Post.builder()
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할사람")
                .build();

        Fields design = Fields.builder()
                .fieldsName("디자인")
                .recruitCount(3)
                .totalAbility(6)
                .post(post)
                .build();

        Fields front = Fields.builder()
                .fieldsName("프론트")
                .recruitCount(1)
                .totalAbility(3)
                .post(post)
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(2)
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

        mockMvc.perform(delete("/post/{id}", savedPost.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        assertThat(postRepository.findAll().size()).isEqualTo(0);
        assertThat(fieldsRepository.findAll().size()).isEqualTo(0);
        assertThat(techStackRepository.findAll().size()).isEqualTo(0);
    }

    @DisplayName("검증이 올바르게 작동한다.")
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(post)))
                .andExpect(status().isBadRequest())
                .andDo(print());

    }
}















