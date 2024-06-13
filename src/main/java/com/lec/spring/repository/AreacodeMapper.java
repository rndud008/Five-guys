package com.lec.spring.repository;

import com.lec.spring.domain.Areacode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AreacodeMapper {
    @Select("SELECT areacode FROM areacode")
    List<Integer> getAllAreacodes();
}