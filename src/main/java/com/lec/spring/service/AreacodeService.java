package com.lec.spring.service;

import com.lec.spring.domain.Areacode;

import java.util.List;

public interface AreacodeService {

    Areacode selectedByAreacode(Long areacode);

    List<Areacode> list();
}
