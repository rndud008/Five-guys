package com.lec.spring.service;

import com.lec.spring.domain.LastCallApiDate;
import org.apache.ibatis.annotations.Param;

public interface LastCallApiDateService {
    int save(LastCallApiDate apiDate);
    LastCallApiDate selectedByUrlAndRegDate(@Param("url") String url, @Param("regdate")String regdate);

}
