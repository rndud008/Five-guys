package com.lec.spring.repository;

import com.lec.spring.domain.Areacode;
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

    // 페이징
    // from 부터 rows 개 만큼 SELECT
    List<Post> selectFromRow(int from, int rows);

    List<Post> selectFromRowArea(int from, int rows, Long areacode);

    // 전체 글의 개수
    int countAll();

    // 지역 게시판 글의 개수
    int countByArea(Long areacode);


}
