package com.lec.spring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

public interface DataService {
    JsonNode fetchApiData(String url) throws IOException, URISyntaxException;

    String fetchApiData(String url, Map<String, String> requestHeaders) throws IOException, URISyntaxException;

    JsonNode naverBlogReview(String responseBody) throws JsonProcessingException;

}
