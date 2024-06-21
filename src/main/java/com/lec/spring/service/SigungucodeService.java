package com.lec.spring.service;

import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.Sigungucode;

import java.io.IOException;
import java.util.List;

public interface SigungucodeService {
    void saveSigungucodes() throws IOException;

    List<Sigungucode> list();

    List<Sigungucode> selectedByAreacode(Long areacode);

    Sigungucode selectedAreacodeBySigungucode(Areacode areacode, Long sigungucode);

}
