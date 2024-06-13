package com.lec.spring.repository;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TravelTypeRepository {

    List<Integer> getAllTravelType();

}
