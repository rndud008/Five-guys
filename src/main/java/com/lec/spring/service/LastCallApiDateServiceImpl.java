package com.lec.spring.service;

import com.lec.spring.domain.LastCallApiDate;
import com.lec.spring.repository.LastCallApiDateRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LastCallApiDateServiceImpl implements LastCallApiDateService {

    private LastCallApiDateRepository lastCallApiDataRepository;

    @Autowired
    public LastCallApiDateServiceImpl(SqlSession sqlSession){
        lastCallApiDataRepository = sqlSession.getMapper(LastCallApiDateRepository.class);
    }

    @Override
    public int save(LastCallApiDate apiData) {


        return 0;
    }
}
