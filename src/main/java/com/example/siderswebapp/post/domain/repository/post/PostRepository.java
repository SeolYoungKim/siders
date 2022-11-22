package com.example.siderswebapp.post.domain.repository.post;

import com.example.siderswebapp.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
