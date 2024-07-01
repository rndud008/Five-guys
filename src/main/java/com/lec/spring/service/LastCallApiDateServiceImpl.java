package com.lec.spring.service;

import com.lec.spring.domain.LastCallApiDate;
import com.lec.spring.repository.LastCallApiDateRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LastCallApiDateServiceImpl implements LastCallApiDateService {

    private LastCallApiDateRepository lastCallApiDateRepository;

    @Autowired
    public LastCallApiDateServiceImpl(SqlSession sqlSession){
        lastCallApiDateRepository = sqlSession.getMapper(LastCallApiDateRepository.class);
    }

    @Override
    public int save(LastCallApiDate apiDate) {

        return lastCallApiDateRepository.save(apiDate);
    }

    @Override
    public LastCallApiDate selectedByUrlAndRegDate(String url, String regdate) {
        return lastCallApiDateRepository.findByUrlAndRegDate(url, regdate);
    }
}
