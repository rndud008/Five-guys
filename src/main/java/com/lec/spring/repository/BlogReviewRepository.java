package com.lec.spring.repository;

import com.lec.spring.domain.BlogReview;
import com.lec.spring.domain.TravelPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BlogReviewRepository {
    int save(BlogReview blogReview);
    // 블로그 리뷰 저장
    int update(BlogReview blogReview);
    // 블로그 리뷰 업데이트
    List<BlogReview> findTravelPostByBlogReview(@Param("travelPost") TravelPost travelPost);
    // 특정 여행정보 블로그 게시물 리스트
    BlogReview findByLinkAndTravelPostId(@Param("link")String link, @Param("id")long id);

}