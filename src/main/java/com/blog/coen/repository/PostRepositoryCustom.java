package com.blog.coen.repository;

import com.blog.coen.domain.Post;
import com.blog.coen.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);

}
