package com.lec.spring.repository;

import com.lec.spring.domain.CommentLike;
import com.lec.spring.domain.Like;

public interface CommentLikeRepository {
    int save(Long userId, Long commentId);
    int delete(Long userId, Long commentId);
    CommentLike findLike(Long userId, Long commentId);
    Long countByComment(Long commentId);

}
