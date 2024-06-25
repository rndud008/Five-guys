package com.lec.spring.repository;

import com.lec.spring.domain.LastCallApiData;

import java.util.List;

public interface LastCallApiDataRepository {
    int save(LastCallApiData lastCallApiData);
    List<LastCallApiData> findAll();
    LastCallApiData findByUrl(String url);


}
