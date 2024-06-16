package com.lec.spring.service;

import com.lec.spring.domain.LastCallApiData;
import com.lec.spring.repository.LastCallApiDataRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LastCallApiDataServiceImpl implements LastCallApiDataService {

    private LastCallApiDataRepository lastCallApiDataRepository;

    @Autowired
    public LastCallApiDataServiceImpl(SqlSession sqlSession){
        lastCallApiDataRepository = sqlSession.getMapper(LastCallApiDataRepository.class);
    }

    @Override
    public int save(LastCallApiData apiData) {


        return 0;
    }
}
