package com.lec.spring.service;

public interface CommentLikeService {

    void likeClick(Long userId, Long commentId);
    Long likeCount(Long commentId);

    // 댓글 좋아요 찾기
    int likeChk(Long userId, Long commentId);

}
