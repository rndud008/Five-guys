package com.lec.spring.repository;

import com.lec.spring.domain.LastCallApiDate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LastCallApiDateRepository {
    int save(LastCallApiDate lastCallApiData);
    List<LastCallApiDate> findAll();
    LastCallApiDate findByUrl(String url);
    LastCallApiDate findByUrlAndRegDate(@Param("url") String url, @Param("regdate")String regdate);

}
