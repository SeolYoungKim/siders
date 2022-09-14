package com.example.siderswebapp.web.controller;

import com.example.siderswebapp.repository.fields.FieldsRepository;
import com.example.siderswebapp.repository.post.PostRepository;
import com.example.siderswebapp.repository.tech_stack.TechStackRepository;
import com.example.siderswebapp.web.request.CreateFieldsRequest;
import com.example.siderswebapp.web.request.CreatePostRequest;
import com.example.siderswebapp.web.request.CreatedTechStackRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                .techStackRequests(new ArrayList<>())
                .build();

        CreateFieldsRequest frontend = CreateFieldsRequest.builder()
                .fieldsName("프론트엔드")
                .recruitCount(3)
                .totalAbility(6)
                .techStackRequests(new ArrayList<>())
                .build();

        CreateFieldsRequest backend = CreateFieldsRequest.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(9)
                .techStackRequests(new ArrayList<>())
                .build();

        CreatePostRequest post = CreatePostRequest.builder()
                .title("제목")
                .recruitType("스터디")
                .contact("010-0000-1111")
                .recruitIntroduction("스터디 구하니까 오셈ㅋ")
                .fieldsRequests(new ArrayList<>())
                .build();

        post.getFieldsRequests().add(design);
        post.getFieldsRequests().add(frontend);
        post.getFieldsRequests().add(backend);

        design.getTechStackRequests().addAll(designStack);
        frontend.getTechStackRequests().addAll(frontendStack);
        backend.getTechStackRequests().addAll(backendStack);


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
}