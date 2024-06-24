package com.lec.spring.service;

import com.lec.spring.domain.Sigungucode;
import com.lec.spring.domain.TravelClassDetail;
import com.lec.spring.domain.TravelPost;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface TravelPostService {
    void saveTravelPosts() throws IOException, URISyntaxException;
    List<TravelPost> selectedTravelTypeByTitleList(TravelClassDetail travelClassDetail,String title);
    List<TravelPost> selectedTravelTypeByAreacodeList(List<TravelClassDetail> travelClassDetailList,
                                                      List<Sigungucode> sigungucodeList, long travelTypeId,int limit, int offset);
    List<TravelPost> selectedTravelTypeByAreacodeAndSearchList(
            List<TravelClassDetail> travelClassDetailList, List<Sigungucode> sigungucodeList,
            long travelTypeId, String searchQuery,int limit, int offset);

    List<TravelPost> selectedTravelTypeByAreacodeAndSigungucodeList(List<TravelClassDetail> travelClassDetailList,
                                                                    Sigungucode sigungucode, long travelTypeId,int limit, int offset);

    List<TravelPost> selectedTravelTypeByAreacodeAndSigungucodeAndSearchList(List<TravelClassDetail> travelClassDetailList, Sigungucode sigungucode,
                                                                    long travelTypeId, String searchQuery,int limit, int offset);

    List<TravelPost> selectedByTravelTypeList(List<TravelClassDetail> travelClassDetailList, long travelTypeId,int limit, int offset);

    List<TravelPost> selectedByTravelTypAndSearchList(List<TravelClassDetail> travelClassDetailList, long travelTypeId, String searchQuery
        ,int limit, int offset);

    TravelPost getTravelPostById(String id) throws IOException;


}
