package com.lec.spring.service;

import com.lec.spring.domain.CommentLike;
import com.lec.spring.repository.CommentLikeRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentLikeServiceImpl implements CommentLikeService {

    private CommentLikeRepository commentLikeRepository;

    @Autowired
    public CommentLikeServiceImpl(SqlSession sqlSession ){
        commentLikeRepository = sqlSession.getMapper(CommentLikeRepository.class);
    }
    @Override
    public void likeClick(Long userId, Long commentId) {
        CommentLike cLike = commentLikeRepository.findLike(userId, commentId);

        if (cLike == null){
            commentLikeRepository.save(userId, commentId);
        } else {
            commentLikeRepository.delete(userId, commentId);
        }
    }

    @Override
    public Long likeCount(Long commentId) {
        return commentLikeRepository.countByComment(commentId);
    }

    @Override
    public int likeChk(Long userId, Long commentId) {
        if(commentLikeRepository.findLike(userId, commentId) == null){
            return 0;
        } else return 1;
    }
}
