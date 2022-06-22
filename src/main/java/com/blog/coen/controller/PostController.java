package com.blog.coen.controller;

import com.blog.coen.domain.Post;
import com.blog.coen.request.PostCreate;
import com.blog.coen.response.PostResponse;
import com.blog.coen.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // Http Method
    // GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD, TRACE, CONNECT
    // 글등록
    // POST Method
    @PostMapping("/posts")
    public /*Map<String, Long>*/ void post(@RequestBody @Valid PostCreate request) throws Exception {
        // Case1. 저장한 데이터 Entity -> response로 응답하기
        // Case2. 저장한 데이터의 primary_id -> response로 응답하기
        //          client 에서는 수신한 id를 글조회 API를 통해서 글 데이터를 수신받음
        // Case3. 응답 필요 없음 -> 클라이언트에서 모든 POST(글) 데이터 context를 잘 관리함.
        // Bad Case: 서버에서 -> 반드시 이렇게 할껍니다! fix
        //          -> 서버에서 차라리 유연하게 대응하는게 좋다 -> 코드를 잘짜자!
        //          -> 한번에 일괄적으로 잘 처리되는 케이스가 없다 -> 잘 관리하는 형태가 중요
//        Long postId = postService.write(request);
//        return Map.of("postId", postId);
        postService.write(request);
    }

    /**
     * /posts -> 글 전체 조회(검색 + 페이징)
     * /posts/{postId}
     */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name="postId") Long id) {
        // Request 클래스 -> PostCreate.java
        // Response 클래스 -> PostResponse.java -> 서비스 정책에 따른 response class

        PostResponse response = postService.get(id);
        // 응답클래스 분리(서비스 정책에 맞는)
        return response;
    }

    /**
     * 조회 API
     * @GetMapping("/posts/{postId}") -> 단건조회
     * @GetMapping("/posts") -> 여러개의 글을 조회하는 API
     */
    @GetMapping("/posts")
    public List<PostResponse> getList() {
        return postService.getList();
    }

}
