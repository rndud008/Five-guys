package com.lec.spring.service;

import com.lec.spring.domain.Areacode;
import com.lec.spring.repository.AreacodeRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AreacodeServiceImpl implements AreacodeService {

    private AreacodeRepository areacodeRepository;

    @Autowired
    public AreacodeServiceImpl(SqlSession sqlSession){
        areacodeRepository = sqlSession.getMapper(AreacodeRepository.class);
    }

    @Override
    public Areacode selectedByAreacode(Long areacode) {

        return areacodeRepository.findByAreaCode(areacode);

    }

    public List<Areacode> list(){

        return areacodeRepository.findAll();

    }






}
