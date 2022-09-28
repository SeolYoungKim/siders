package com.example.siderswebapp.domain.post;

import com.example.siderswebapp.domain.Ability;
import com.example.siderswebapp.domain.RecruitType;
import com.example.siderswebapp.domain.fields.Fields;
import com.example.siderswebapp.domain.tech_stack.TechStack;
import com.example.siderswebapp.repository.fields.FieldsRepository;
import com.example.siderswebapp.repository.post.PostRepository;
import com.example.siderswebapp.repository.tech_stack.TechStackRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class PostTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FieldsRepository fieldsRepository;

    @Autowired
    private TechStackRepository techStackRepository;

    @DisplayName("글 저장 시 모든 정보가 제대로 저장된다.")
    @Test
    void mappingTestSave() {
        //given
        Post post = Post.builder()
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할사람")
                .build();

        Fields design = Fields.builder()
                .fieldsName("디자인")
                .recruitCount(3)
                .totalAbility(Ability.LOW)
                .post(post)
                .build();

        Fields front = Fields.builder()
                .fieldsName("프론트")
                .recruitCount(1)
                .totalAbility(Ability.LOW)
                .post(post)
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(Ability.HIGH)
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

        //when
        List<Fields> fieldsList = postRepository.findAll().get(0).getFieldsList();
        TechStack techStack1 = design.getStacks().get(0);
        TechStack techStack2 = fieldsList.get(1).getStacks().get(0);
        TechStack techStack3 = fieldsList.get(2).getStacks().get(0);

        //then
        // 각 리포지토리에 저장이 잘 되어 있다.
        assertThat(postRepository.findAll().size()).isEqualTo(1);
        assertThat(fieldsRepository.findAll().size()).isEqualTo(3);
        assertThat(techStackRepository.findAll().size()).isEqualTo(3);

        // post로부터 불러들여온 fieldsList에 field가 잘 저장되어 있다.
        assertThat(fieldsList.size()).isEqualTo(3);

        // 각 field 별로 stack이 잘 저장되어 있다.
        assertThat(techStack1.getStackName()).isEqualTo("zeplin");
        assertThat(techStack2.getStackName()).isEqualTo("react");
        assertThat(techStack3.getStackName()).isEqualTo("spring");
    }

    @DisplayName("글 삭제 시 모든 정보가 전부 함께 삭제된다.")
    @Test
    void mappingTestDelete() {
        //given
        Post post = Post.builder()
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할사람")
                .build();

        Fields design = Fields.builder()
                .fieldsName("디자인")
                .recruitCount(3)
                .totalAbility(Ability.LOW)
                .post(post)
                .build();

        Fields front = Fields.builder()
                .fieldsName("프론트")
                .recruitCount(1)
                .totalAbility(Ability.LOW)
                .post(post)
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(Ability.LOW)
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

        //when
        postRepository.delete(post);

        //then
        assertThat(postRepository.findAll().size()).isEqualTo(0);
        assertThat(fieldsRepository.findAll().size()).isEqualTo(0);
        assertThat(techStackRepository.findAll().size()).isEqualTo(0);

    }

    @DisplayName("필드 삭제는 글에 영향을 주지 않는다.")
    @Test
    void mappingTestField() {
        //given
        Post post = Post.builder()
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할사람")
                .build();

        Fields design = Fields.builder()
                .fieldsName("디자인")
                .recruitCount(3)
                .totalAbility(Ability.LOW)
                .post(post)
                .build();

        Fields front = Fields.builder()
                .fieldsName("프론트")
                .recruitCount(1)
                .totalAbility(Ability.LOW)
                .post(post)
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(Ability.LOW)
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

        //when
        post.getFieldsList().remove(design);

        //then
        assertThat(postRepository.findAll().size()).isEqualTo(1);
        assertThat(fieldsRepository.findAll().size()).isEqualTo(2);
        assertThat(postRepository.findAll().get(0).getFieldsList().size()).isEqualTo(2);
        assertThat(techStackRepository.findAll().size()).isEqualTo(2);
    }

    @DisplayName("기술 스택 삭제는 글과 필드에 영향을 주지 않는다.")
    @Test
    void mappingTestStack() {
        //given
        Post post = Post.builder()
                .title("제목")
                .recruitType(RecruitType.STUDY)
                .contact("010.0000.0000")
                .recruitIntroduction("공부할사람")
                .build();

        Fields design = Fields.builder()
                .fieldsName("디자인")
                .recruitCount(3)
                .totalAbility(Ability.LOW)
                .post(post)
                .build();

        Fields front = Fields.builder()
                .fieldsName("프론트")
                .recruitCount(1)
                .totalAbility(Ability.LOW)
                .post(post)
                .build();

        Fields back = Fields.builder()
                .fieldsName("백엔드")
                .recruitCount(1)
                .totalAbility(Ability.LOW)
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

        //when
        design.getStacks().remove(zeplin);
        front.getStacks().remove(react);

        //then
        assertThat(postRepository.findAll().size()).isEqualTo(1);
        assertThat(fieldsRepository.findAll().size()).isEqualTo(3);
        assertThat(postRepository.findAll().get(0).getFieldsList().size()).isEqualTo(3);
        assertThat(techStackRepository.findAll().size()).isEqualTo(1);
    }

}