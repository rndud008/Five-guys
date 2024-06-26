package com.lec.spring.service;

public interface CommentLikeService {

    void likeClick(Long userId, Long commentId);
    Long likeCount(Long commentId);

}
