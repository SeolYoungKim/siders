package com.example.siderswebapp.repository.post;

import com.example.siderswebapp.domain.Ability;
import com.example.siderswebapp.domain.RecruitType;
import com.example.siderswebapp.domain.fields.Fields;
import com.example.siderswebapp.domain.member.Member;
import com.example.siderswebapp.domain.member.RoleType;
import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.repository.fields.FieldsRepository;
import com.example.siderswebapp.repository.member.MemberRepository;
import com.example.siderswebapp.web.request.post.update.UpdateFieldsRequest;
import com.example.siderswebapp.web.request.post.update.UpdatePostRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class PostRepositoryImplTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FieldsRepository fieldsRepository;

    @Autowired
    private MemberRepository memberRepository;


    @Test
    void jpaTest() {
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

        //given
        Post posts = Post.builder()
                .contact("123")
                .title("q1e")
                .recruitType(RecruitType.STUDY)
                .recruitIntroduction("sdfsdf")
                .isCompleted(false)
                .member(savedMember)
                .build();


        Fields fields = fieldsRepository.findById(-1L)
                .orElse(Fields.builder()
                        .post(posts)
                        .recruitCount(1)
                        .totalAbility(Ability.LOW)
                        .fieldsName("name")
                        .build());

        postRepository.save(posts);

        Post post = postRepository.findById(posts.getId())
                .orElseGet(() -> Post.builder()
                        .contact("123")
                        .title("q1e")
                        .recruitType(RecruitType.STUDY)
                        .recruitIntroduction("sdfsdf")
                        .build());

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
                .totalAbility("High")
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