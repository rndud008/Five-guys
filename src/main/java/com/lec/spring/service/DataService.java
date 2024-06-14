package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.net.URISyntaxException;

public interface DataService {
    JsonNode fetchApiData(String url) throws IOException, URISyntaxException;

}
