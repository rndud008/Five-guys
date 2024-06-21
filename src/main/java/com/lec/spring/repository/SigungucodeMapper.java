package com.lec.spring.repository;

import com.lec.spring.domain.Sigungucode;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SigungucodeMapper {

    void insertSigungucode(Sigungucode sigungucode);

}
