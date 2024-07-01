package com.lec.spring.repository;

import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.Sigungucode;

import java.util.List;


public interface SigungucodeRepository {
    int save(Sigungucode sigungucode);

    int update(Sigungucode sigungucode);

    List<Sigungucode> findAll();

    List<Sigungucode> findByAreacode(Long areacode);

    Sigungucode findAreacodeBySigungucode(Areacode areacode,Long sigungucode);
    // 지역코드에 참조하는 시군구 단일객체 호출.

}
