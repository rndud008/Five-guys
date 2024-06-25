package com.lec.spring.service;

import com.lec.spring.domain.Areacode;
import com.lec.spring.repository.AreacodeRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AreaServiceImpl implements AreaService {

    private AreacodeRepository areacodeRepository;

    @Autowired
    public AreaServiceImpl(SqlSession sqlSession){
        areacodeRepository = sqlSession.getMapper(AreacodeRepository.class);
    }
    @Override
    public Areacode findByAreaCode(Long areacode) {

        return areacodeRepository.findByAreaCode(areacode);
    }
}
