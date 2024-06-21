package com.lec.spring.repository;

import com.lec.spring.domain.BlogReview;
import com.lec.spring.domain.TravelPost;

import java.util.List;

public interface BlogReviewRepository {
    int save(BlogReview blogReview);
    // 블로그 리뷰 저장
    int update(BlogReview blogReview);
    // 블로그 리뷰 업데이트
    List<BlogReview> findPostTitleByBlogReview(TravelPost travelPost, String title);
    // 특정 여행정보타이틀 블로그 게시물 리스트
    BlogReview findByLink(String link);

}
