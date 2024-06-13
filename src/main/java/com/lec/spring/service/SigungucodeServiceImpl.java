package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lec.spring.domain.Sigungucode;
import com.lec.spring.repository.AreacodeMapper;
import com.lec.spring.repository.SigungucodeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
    public class SigungucodeServiceImpl implements SigungucodeService {
        private final SigungucodeMapper sigungucodeMapper;
        private final AreacodeMapper areacodeMapper;
        private final ApiService apiService;

        public SigungucodeServiceImpl(SigungucodeMapper sigungucodeMapper, AreacodeMapper areacodeMapper, ApiService apiService) {
            this.sigungucodeMapper = sigungucodeMapper;
            this.areacodeMapper = areacodeMapper;
            this.apiService = apiService;
        }

        @Override
        @Transactional
        public void saveSigungucodes() throws IOException {
            List<Integer> areacodes = areacodeMapper.getAllAreacodes();

            for (int areacode : areacodes) {
                JsonNode items = apiService.fetchsigungu(areacode);

                for (JsonNode item : items) {
                    Sigungucode sigungucode = new Sigungucode();

                    sigungucode.setAreacode(areacode);
                    sigungucode.setSigungucode(item.get("code").asInt());
                    sigungucode.setName(item.get("name").asText());

                    sigungucodeMapper.insertSigungucode(sigungucode);
                }
            }
        }
    }
