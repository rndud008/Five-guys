package com.lec.spring.service;

import com.lec.spring.domain.Sigungucode;
import com.lec.spring.domain.TravelClassDetail;
import com.lec.spring.domain.TravelPost;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface TravelPostService {
    void saveTravelPosts() throws IOException, URISyntaxException;
    List<TravelPost> selectedTravelTypeByTitleList(TravelClassDetail travelClassDetail,String title);
    List<TravelPost> selectedTravelTypeByAreacodeList(List<TravelClassDetail> travelClassDetailList,
                                                      List<Sigungucode> sigungucodeList);
    List<TravelPost> selectedTravelTypeByAreacodeAndSigungucodeList(List<TravelClassDetail> travelClassDetailList,
                                                                    Sigungucode sigungucode);
    List<TravelPost> selectedByTravelTypeList(List<TravelClassDetail> travelClassDetailList);



}
