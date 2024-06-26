package com.lec.spring.repository;

import com.lec.spring.domain.Like;

public interface LikeRepository {
    int save(Long userId, Long postId);
    int delete(Long userId, Long postId);
    Long countByPost(Long postId);
    Like findLike(Long userId, Long postId);
}
