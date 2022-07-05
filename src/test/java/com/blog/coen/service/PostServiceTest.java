package com.blog.coen.service;

import com.blog.coen.domain.Post;
import com.blog.coen.exception.PostNotFound;
import com.blog.coen.repository.PostRepository;
import com.blog.coen.request.PostCreate;
import com.blog.coen.request.PostEdit;
import com.blog.coen.request.PostSearch;
import com.blog.coen.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PostServiceTest  {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // when
        postService.write(postCreate);

        // then
        Assertions.assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);

        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 한개 조회")
    void test2() {
        // given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        // when
        PostResponse response = postService.get(requestPost.getId());

        // then
        Assertions.assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());

    }

    @Test
    @DisplayName("글 여러개 조회")
    void test3() throws Exception {
        // given
//        Post requestPost1 = Post.builder()
//                .title("foo1")
//                .content("bar1")
//                .build();
//        postRepository.save(requestPost1);
//
//        Post requestPost2 = Post.builder()
//                .title("foo2")
//                .content("bar2")
//                .build();
//        postRepository.save(requestPost2);

        // 위 -> 아래로 변경

        postRepository.saveAll(List.of(
                Post.builder()
                        .title("foo1")
                        .content("bar1")
                        .build(),
                Post.builder()
                        .title("foo2")
                        .content("bar2")
                        .build()
        ));

        // when
        List<PostResponse> posts = postService.getList();


        // then
        assertEquals(2L, posts.size());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test4() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31) // for(int i = 0; i < 30; i++)과 같음
                .mapToObj(i -> Post.builder()
                        .title("호돌맨 제목 " + i)
                        .content("반포자이 " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // sql -> select, limit, offset 은 알아야함ㅋ

        PostSearch postSearch = PostSearch.builder()
                .build();

        // when
        List<PostResponse> posts = postService.getList(postSearch);


        // then
        assertEquals(10L, posts.size());
        assertEquals("호돌맨 제목 30", posts.get(0).getTitle());
        assertEquals("호돌맨 제목 26", posts.get(4).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test5() throws Exception {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("반포자이")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id = " + post.getId()));
        Assertions.assertEquals("호돌걸", changedPost.getTitle());
        Assertions.assertEquals("반포자이", changedPost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test6() throws Exception {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌맨")
                .content("초가집")
                .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id = " + post.getId()));
        Assertions.assertEquals("호돌맨", changedPost.getTitle());
        Assertions.assertEquals("초가집", changedPost.getContent());
    }

    @Test
    @DisplayName("글 삭제")
    void test7() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // then
        Assertions.assertEquals(0, postRepository.count());


    }

    @Test
    @DisplayName("글 한개 조회 - 존재하지 않는 글")
    void test8() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        // when
//        PostResponse response = postService.get(post.getId() + 1L);

        // then
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        });

    }

    @Test
    @DisplayName("글 삭제 - 존재하지 않는 글")
    void test9() {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        // when


        // then
        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);
        });
    }


    @Test
    @DisplayName("글 내용 수정 - 존재하지 않는 글")
    void test10() throws Exception {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("호돌맨")
                .content("초가집")
                .build();

        // when

        Assertions.assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1L, postEdit);
        });
    }
}