package com.blog.coen.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {

    private final String title;
    private final String content;

    @Builder
    public PostEditor(String title, String content) { // 여기서 null 체크를 해도 됨
        this.title = title;
        this.content = content;
    }
}
