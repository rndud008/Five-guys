package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lec.spring.domain.TravelClassDetail;
import com.lec.spring.repository.TravelClassDetailRepository;
import com.lec.spring.repository.TravelTypeRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TravelClassDetailServiceImpl implements TravelClassDetailService {
    private final TravelTypeRepository travelTypeRepository;
    private final TravelClassDetailRepository travelClassDetailRepository;
    private final ApiService apiService;

    public TravelClassDetailServiceImpl(TravelTypeRepository travelTypeRepository, TravelClassDetailRepository travelClassDetailRepository, ApiService apiService) {
        this.travelTypeRepository = travelTypeRepository;
        this.travelClassDetailRepository = travelClassDetailRepository;
        this.apiService = apiService;
    }

    @Override
    public void saveTravelClassDetail() throws IOException {
        List<Integer> type = travelTypeRepository.getAllTravelType();

        for(int types : type){
            JsonNode items = apiService.fetchtype(types);

            for(JsonNode item : items){
                TravelClassDetail travelClassDetail = new TravelClassDetail();

                travelClassDetail.setTravel_type_id((long) types);
                travelClassDetail.setName(item.get("name").asText());
                travelClassDetail.setCode(item.get("code").asText());

                travelClassDetailRepository.insertTravelClassDetail(travelClassDetail);
            }
        }

    }
}
