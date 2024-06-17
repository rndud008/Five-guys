package com.lec.spring.service;

import com.lec.spring.repository.LastCallApiDataRepository;
import com.lec.spring.repository.TravelPostRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlogReviewServiceImpl implements BlogReviewService {

    private TravelPostRepository travelPostRepository;
    private LastCallApiDataRepository lastCallApiDataRepository;
    private DataService dataService;

    @Autowired
    public BlogReviewServiceImpl(SqlSession sqlSession, DataService dataService){
        travelPostRepository = sqlSession.getMapper(TravelPostRepository.class);
        lastCallApiDataRepository = sqlSession.getMapper(LastCallApiDataRepository.class);
        this.dataService = dataService;
    }

    @Override
    public void blogReviewSaves() {



    }
}
