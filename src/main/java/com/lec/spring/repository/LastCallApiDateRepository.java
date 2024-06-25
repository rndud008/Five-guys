package com.lec.spring.repository;

import com.lec.spring.domain.LastCallApiDate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LastCallApiDateRepository {
    int save(LastCallApiDate lastCallApiData);

    List<LastCallApiDate> findAll();
    LastCallApiDate findByUrl(String url);
    LastCallApiDate findByUrlAndAreacode(String url, @Param("areacode") Long areacode); // 수정된 메서드 시그니처

    LastCallApiDate findByRegdate(String date);

}
