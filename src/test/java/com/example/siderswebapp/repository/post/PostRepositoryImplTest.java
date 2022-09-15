package com.example.siderswebapp.repository.post;

import com.example.siderswebapp.domain.RecruitType;
import com.example.siderswebapp.domain.fields.Fields;
import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.repository.fields.FieldsRepository;
import com.example.siderswebapp.web.request.update.UpdateFieldsRequest;
import com.example.siderswebapp.web.request.update.UpdatePostRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class PostRepositoryImplTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FieldsRepository fieldsRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    void jpaTest() {

        //given
        Post posts = Post.builder()
                .contact("123")
                .title("q1e")
                .recruitType(RecruitType.STUDY)
                .recruitIntroduction("sdfsdf")
                .build();


        Fields fields = fieldsRepository.findById(-1L)
                .orElse(Fields.builder()
                        .post(posts)
                        .recruitCount(1)
                        .totalAbility(5)
                        .fieldsName("name")
                        .build());

        postRepository.save(posts);

        Post post = postRepository.findById(posts.getId()).orElse(null);

        UpdatePostRequest updatePostRequest = UpdatePostRequest.builder()
                .fieldsList(new ArrayList<>())
                .recruitType("스터디")
                .contact("email")
                .recruitIntroduction("ssss")
                .title("tttt")
                .build();

        UpdateFieldsRequest dto = UpdateFieldsRequest.builder()
                .stacks(new ArrayList<>())
                .id(fields.getId())
                .totalAbility(3)
                .recruitCount(1)
                .fieldsName("34")
                .build();

        updatePostRequest.getFieldsList().add(dto);

        post.updatePost(updatePostRequest);
        fields.updateFields(dto);

        //when
        postRepository.save(post);

        //then
        assertThat(post.getFieldsList().size()).isEqualTo(1);
    }
}