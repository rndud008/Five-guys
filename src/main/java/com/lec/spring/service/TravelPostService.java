package com.lec.spring.service;

import com.lec.spring.domain.TravelClassDetail;
import com.lec.spring.domain.TravelPost;

import java.io.IOException;
import java.net.URISyntaxException;

public interface TravelPostService {
    void saveTravelPosts() throws IOException, URISyntaxException;

    TravelPost getTravelPostById(String id) throws IOException;


}
