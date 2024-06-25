package com.lec.spring.repository;

import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.LastCallApiData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LastCallApiDataRepository {
    int save(LastCallApiData lastCallApiData);
    List<LastCallApiData> findAll();
    LastCallApiData findByUrl(String url);
    LastCallApiData findByUrlAndAreacode(String url, @Param("areacode") Long areacode); // 수정된 메서드 시그니처



}
