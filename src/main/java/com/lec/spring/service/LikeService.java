package com.lec.spring.service;

public interface LikeService {

    // 좋아요 토글
    int likeClick(Long userId, Long postId);

    // 글별 좋아요 개수
    Long likeCount(Long postId);

    // 좋아요 찾기
    int likeChk(Long userId, Long postId);
}
