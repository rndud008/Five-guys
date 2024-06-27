package com.lec.spring.service;

import com.lec.spring.domain.BlogReview;
import com.lec.spring.domain.TravelPost;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface BlogReviewService {

    void blogReviewSaves() throws IOException, URISyntaxException;

    List<BlogReview> selectedTravelPostByBlogReview(TravelPost travelPost, int offset, int limit);

    List<BlogReview> getsumBlogReview(TravelPost travelPost);


}
