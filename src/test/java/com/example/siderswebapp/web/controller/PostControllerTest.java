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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @DisplayName("글이 페이징되어 여러건 조회된다. 쿼리 파라미터를 넘기지 않아도 기본값이 적용된다.")
    @Test
    void getPostList() throws Exception {
        List<Post> postList = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("title " + i)
                        .recruitType(RecruitType.STUDY)
                        .contact("email")
                        .recruitIntroduction("content " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(postList);

        mockMvc.perform(get("/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].title").value("title 30"))
                .andExpect(jsonPath("$.[9].title").value("title 21"))
                .andDo(print());
    }
}