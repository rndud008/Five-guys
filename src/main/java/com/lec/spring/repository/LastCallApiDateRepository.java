package com.lec.spring.repository;

import com.lec.spring.domain.LastCallApiDate;

import java.util.List;

public interface LastCallApiDateRepository {
    int save(LastCallApiDate lastCallApiData);
    List<LastCallApiDate> findAll();
    LastCallApiDate findByUrl(String url);


}
