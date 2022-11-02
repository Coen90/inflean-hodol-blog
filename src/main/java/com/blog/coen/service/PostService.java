package com.blog.coen.service;

import com.blog.coen.domain.Post;
import com.blog.coen.domain.PostEditor;
import com.blog.coen.exception.PostNotFound;
import com.blog.coen.repository.PostRepository;
import com.blog.coen.request.PostCreate;
import com.blog.coen.request.PostEdit;
import com.blog.coen.request.PostSearch;
import com.blog.coen.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        // postCreate -> Entity
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
//        return post.getId();
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public List<PostResponse> getList() {
//        return postRepository.findAll().stream()
//                .map(post -> PostResponse.builder()
//                            .id(post.getId())
//                            .title(post.getTitle())
//                            .content(post.getContent())
//                            .build())
//                .collect(Collectors.toList());
        return postRepository.findAll().stream() // PostResponse에 생성자 오버로딩을 만들어 Post 객체를 넘겨주고, PostResponse 클래스에서 만들기
                // .map(post -> new PostResponse(post))
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

//    public List<PostResponse> getList(Pageable pageable) {
//        // web -> page 1 -> 0
////        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));
//
//        return postRepository.findAll(pageable).stream() // PostResponse에 생성자 오버로딩을 만들어 Post 객체를 넘겨주고, PostResponse 클래스에서 만들기
//                // .map(post -> new PostResponse(post))
//                .map(PostResponse::new)
//                .collect(Collectors.toList());
//    }

//    public List<PostResponse> getList(Pageable pageable) {
//        return postRepository.getList(1).stream()
//                .map(PostResponse::new)
//                .collect(Collectors.toList());
//    }
//
    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new); // () -> new PostNotFound()  ==  PostNotFound::new

        PostEditor.PostEditorBuilder postEditorBuilder = post.toEditor();

        PostEditor postEditor = postEditorBuilder
                .title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);

        return new PostResponse(post);

//        이런 방법도 존재함 (타이틀, 콘텐츠 중 하나만 올 경우)
//        if(postEdit.getTitle() != null) {
//            postEditorBuilder.title(postEdit.getTitle());
//        }
//
//        if(postEdit.getContent() != null) {
//            postEditorBuilder.content(postEdit.getContent());
//        }
//
//        post.edit(postEditorBuilder.build());
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        // 존재하는 경우
        postRepository.delete(post);
    }

}
