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
    public void likeClick(Long userId, Long postId) {
        System.out.println(postId + " " + userId);

        Like like = likeRepository.findLike(userId, postId);

        System.out.println(like);

        if(like == null){
            likeRepository.save(userId, postId);
        } else {
            likeRepository.delete(userId, postId);
        }
    }

    @Override
    public Long likeCount(Long postId) {
        return likeRepository.countByPost(postId);
    }
}
