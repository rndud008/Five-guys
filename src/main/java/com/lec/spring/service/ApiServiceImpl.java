package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;



@Service
public class ApiServiceImpl implements ApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ApiServiceImpl(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode fetchApiData(int areaCode) throws IOException {
        String apiUrl = String.format("https://apis.data.go.kr/B551011/KorService1/areaCode1?serviceKey=YOUR_SERVICE_KEY&numOfRows=30&pageNo=1&MobileOS=ETC&MobileApp=AppTest&areaCode=%d&_type=json", areaCode);
        String response = restTemplate.getForObject(apiUrl, String.class);
        JsonNode rootNode = objectMapper.readTree(response);
        return rootNode.path("response").path("body").path("items").path("item");
    }
}