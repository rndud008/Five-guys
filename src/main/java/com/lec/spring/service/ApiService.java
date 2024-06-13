package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public interface ApiService {
    JsonNode fetchApiData(int areacode) throws IOException;
}
