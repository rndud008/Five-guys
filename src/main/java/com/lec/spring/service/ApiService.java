package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public interface ApiService {
    JsonNode fetchsigungu(int areacode) throws IOException;

    JsonNode fetchtype(int areacode) throws IOException;

}
