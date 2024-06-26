package com.lec.spring.service;

import com.lec.spring.domain.Areacode;

public interface AreaService {
    Areacode findByAreaCode(Long areacode);
}
