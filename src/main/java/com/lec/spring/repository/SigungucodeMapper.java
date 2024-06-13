package com.lec.spring.repository;

import com.lec.spring.domain.Sigungucode;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SigungucodeMapper {
    @Insert("INSERT INTO sigungucode (areacode, sigungucode, name) VALUES (#{areacode}, #{sigungucode}, #{name})")
    void insertSigungucode(Sigungucode sigungucode);

}
