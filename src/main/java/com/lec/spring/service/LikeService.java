package com.lec.spring.service;

public interface LikeService {

    // 좋아요 토글
    void likeClick(Long userId, Long postId);

    // 글별 좋아요 개수
    Long likeCount(Long postId);
}
