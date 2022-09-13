package com.example.siderswebapp.repository.post;

import com.example.siderswebapp.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
