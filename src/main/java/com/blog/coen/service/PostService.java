package com.blog.coen.service;

import com.blog.coen.domain.Post;
import com.blog.coen.repository.PostRepository;
import com.blog.coen.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        // postCreate -> Entity
        Post post = new Post(postCreate.getTitle(), postCreate.getContent());

        postRepository.save(post);
    }
}
