package com.lec.spring.repository;

import com.lec.spring.domain.Comment;
import com.lec.spring.domain.User;

import java.util.List;

public interface CommentRepository {

    // 특정 게시판의 댓글들
    List<Comment> findByPost(Long travel_diary_post_id);

    // comment 저장
    int save(Comment comment);

    // 특정 comment 삭제
    int deleteById(Long id);

}
