package com.lec.spring.service;

import com.lec.spring.domain.TravelType;
import com.lec.spring.repository.TravelTypeRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TravelTypeServiceImpl implements TravelTypeService {

    private TravelTypeRepository travelTypeRepository;
    @Autowired
    public TravelTypeServiceImpl(SqlSession sqlSession){
        travelTypeRepository = sqlSession.getMapper(TravelTypeRepository.class);
    }
    @Override
    public List<TravelType> list() {
        return travelTypeRepository.findAll();
    }

    @Override
    public TravelType selectById(Long id) {
        return travelTypeRepository.findById(id);
    }
}
