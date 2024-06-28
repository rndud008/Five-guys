package com.lec.spring.service;

import com.lec.spring.domain.Like;
import com.lec.spring.repository.LikeRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    private LikeRepository likeRepository;

    @Autowired
    public LikeServiceImpl(SqlSession sqlSession){
        likeRepository = sqlSession.getMapper(LikeRepository.class);
    }

    @Override
    public int likeClick(Long userId, Long postId) {

        Like like = likeRepository.findLike(userId, postId);

        if(like == null){
            return likeRepository.save(userId, postId);
        } else {
            return likeRepository.delete(userId, postId);
        }
    }

    @Override
    public Long likeCount(Long postId) {

        return likeRepository.countByPost(postId);
    }

    @Override
    public int likeChk(Long userId, Long postId) {
        if(likeRepository.findLike(userId, postId) == null){
            return 0;
        } else return 1;
    }
}
