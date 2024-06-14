package com.lec.spring.repository;

import com.lec.spring.domain.Post;

import java.util.List;

public interface PostRepository {

    int save(Post post);

    Post findById(Long id);

    int incViewCnt(Long id);

    List<Post> findAll();

    List<Post> findByAreacode(Long areacode);

    int update(Post post);

    int delete(Post post);


}
