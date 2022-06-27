package com.blog.coen.controller;

import com.blog.coen.domain.Post;
import com.blog.coen.repository.PostRepository;
import com.blog.coen.request.PostCreate;
import com.blog.coen.request.PostEdit;
import com.blog.coen.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다.")
    void test() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request); // PostCreate의 Getter를 사용하여 해당 argument 들을 json 형태로 불러온다.

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                ) // application/json
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print()); // 실패시에 나오는 것처럼 성공해도 프린트함
    }


    @Test
    @DisplayName("/posts 요청시 title 값은 필수다.")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                // "", null 체크해줌(@NotBlank)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400")) // junit5 jsonPath
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다.")) // junit5 jsonPath
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요.")) // junit5 jsonPath
                .andDo(print()); // 실패시에 나오는 것처럼 성공해도 프린트함
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    void test03() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                // "", null 체크해줌(@NotBlank)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print()); // 실패시에 나오는 것처럼 성공해도 프린트함

        // then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }


    @Test
    @DisplayName("글 한개 조회")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();
        postRepository.save(post);

        // 클라이언트 요구사항
            // json 응답에서 title값 길이를 최대 10글자로 해주세요.


        // expected(when + then)
        mockMvc.perform(get("/posts/{postId}", post.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        // given
        Post requestPost1 = postRepository.save(Post.builder()
                .title("title_1")
                .content("content_1")
                .build());

        Post requestPost2 = postRepository.save( Post.builder()
                .title("title_2")
                .content("content_2")
                .build());

        // 클라이언트 요구사항
            // json 응답에서 title값 길이를 최대 10글자로 해주세요.

        /**
         * {id: ..., title: ...}
         */
        /**
         * [{id: ..., title: ...}, {id: ..., title: ...}, ...]
         */
        // expected(when + then)
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].id").value(requestPost1.getId()))
                .andExpect(jsonPath("$[0].title").value("title_1"))
                .andExpect(jsonPath("$[0].content").value("content_1"))
                .andExpect(jsonPath("$[1].id").value(requestPost2.getId()))
                .andExpect(jsonPath("$[1].title").value("title_2"))
                .andExpect(jsonPath("$[1].content").value("content_2"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test6() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31) // for(int i = 0; i < 30; i++)과 같음
                .mapToObj(i -> Post.builder()
                        .title("호돌맨 제목 " + i)
                        .content("반포자이 " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // expected(when + then)
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].id", Matchers.is(30)))
                .andExpect(jsonPath("$[0].title", Matchers.is("호돌맨 제목 30")))
                .andExpect(jsonPath("$[0].content", Matchers.is("반포자이 30")))
                .andDo(print());
    }
    @Test
    @DisplayName("페이지를 0으로 요청하면 첫페이지를 가져온다.")
    void test7() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31) // for(int i = 0; i < 30; i++)과 같음
                .mapToObj(i -> Post.builder()
                        .title("호돌맨 제목 " + i)
                        .content("반포자이 " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        // expected(when + then)
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].id", Matchers.is(30)))
                .andExpect(jsonPath("$[0].title", Matchers.is("호돌맨 제목 30")))
                .andExpect(jsonPath("$[0].content", Matchers.is("반포자이 30")))
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test8() throws Exception {
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

        // expected(when + then)
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 삭제")
    void test9() throws Exception {
        // given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();

        postRepository.save(post);

        // expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                    .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test10() throws Exception {
        // expected
        mockMvc.perform(delete("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test11() throws Exception {
        // given
        PostEdit postEdit = PostEdit.builder()
                .title("호돌걸")
                .content("반포자이")
                .build();

        // expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("게시글 작성시 제목에 '바보'는 포함될 수 없다.")
    void test12() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .title("나는 바보입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print()); // 실패시에 나오는 것처럼 성공해도 프린트함
    }
}