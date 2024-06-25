package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lec.spring.domain.BlogReview;
import com.lec.spring.domain.LastCallApiDate;
import com.lec.spring.domain.TravelPost;
import com.lec.spring.repository.BlogReviewRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlogReviewTransacionService {

    private BlogReviewRepository blogReviewRepository;

    @Autowired
    public BlogReviewTransacionService(SqlSession sqlSession){
        blogReviewRepository = sqlSession.getMapper(BlogReviewRepository.class);
    }

    @Transactional
    public void itemSave(JsonNode item, TravelPost travelPost, LastCallApiDate lastCallApiData){
        BlogReview blogReview = new BlogReview();
        blogReview.setTitle(item.get("title").asText());
        blogReview.setLink(item.get("link").asText());
        blogReview.setDescription(item.get("description").asText());
        blogReview.setPostdate(item.get("postdate").asText());
        blogReview.setTravelPost(travelPost);
        blogReview.setLastCallApiData(lastCallApiData);
        blogReviewRepository.save(blogReview);

    }

}
