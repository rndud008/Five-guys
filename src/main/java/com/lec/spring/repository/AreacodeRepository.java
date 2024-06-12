package com.lec.spring.repository;

import com.lec.spring.domain.Areacode;

import java.util.List;

public interface AreacodeRepository {

    Areacode findByAreaCode(Long areaCode); // Areacode를 받고 지역을 리턴

    List<Areacode> findAll(); // 전체 목록조회

}