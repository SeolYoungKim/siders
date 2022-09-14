package com.example.siderswebapp.repository.post;

import com.example.siderswebapp.domain.RecruitType;
import com.example.siderswebapp.domain.post.Post;
import com.example.siderswebapp.web.request.search.PostSearch;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PostRepositoryImplTest {
    @Autowired
    private PostRepository postRepository;

    @DisplayName("페이징이 잘 된다.")
    @Test
    void pagingOk() {
        List<Post> postList = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("title " + i)
                        .recruitType(RecruitType.STUDY)
                        .contact("email")
                        .recruitIntroduction("content " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(postList);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();

        List<Post> pagingList = postRepository.getPostList(postSearch);
        assertThat(pagingList.size()).isEqualTo(10);
        assertThat(pagingList.get(0).getTitle()).isEqualTo("title 30");
        assertThat(pagingList.get(9).getTitle()).isEqualTo("title 21");
    }

    @DisplayName("page = null, size = null 이어도 NPE가 터지지 않고 페이징이 잘 된다.")
    @Test
    void nullPagingOk() {
        List<Post> postList = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("title " + i)
                        .recruitType(RecruitType.STUDY)
                        .contact("email")
                        .recruitIntroduction("content " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(postList);

        PostSearch postSearch = PostSearch.builder()
                .build();

        List<Post> pagingList = postRepository.getPostList(postSearch);
        assertThat(pagingList.size()).isEqualTo(10);
        assertThat(pagingList.get(0).getTitle()).isEqualTo("title 30");
        assertThat(pagingList.get(9).getTitle()).isEqualTo("title 21");
    }
}