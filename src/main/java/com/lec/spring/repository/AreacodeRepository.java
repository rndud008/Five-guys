package com.lec.spring.repository;

import com.lec.spring.domain.Areacode;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AreacodeRepository {

    Areacode findByAreaCode(Long areacode); // Areacode를 받고 지역을 리턴

    Areacode findByName(String name); // 지역 이름으로 Areacode를 조회하여 리턴

    List<Areacode> findAll(); // 전체 지역목록조회
    List<Areacode> findAllArea(); // 전체 목록조회 이름 헷갈려서 바꿈

}
