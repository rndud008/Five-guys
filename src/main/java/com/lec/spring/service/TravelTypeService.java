package com.lec.spring.service;

import com.lec.spring.domain.TravelType;

import java.util.List;

public interface TravelTypeService {

    List<TravelType> list();

    TravelType selectById(Long id);




}
