package com.lec.spring.controller;


import com.lec.spring.domain.BlogReview;
import com.lec.spring.domain.TravelPost;
import com.lec.spring.service.BlogReviewService;
import com.lec.spring.service.TravelPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Controller
public class BlogController {
    @Autowired
    private TravelPostService travelPostService;
    @Autowired
    private BlogReviewService blogReviewService;

    @RestController
    public class BlogRestController {

        @GetMapping("/api/blogs")
        public List<BlogReview> getMoreBlogs(@RequestParam("id") String id, @RequestParam("offset") int offset) throws IOException {

            TravelPost travelPost = travelPostService.getTravelPostBycontentId(id);
            System.out.println(blogReviewService.selectedTravelPostByBlogReview(travelPost, offset, 3));

            return blogReviewService.selectedTravelPostByBlogReview(travelPost, offset, 3);
        }

        @GetMapping("/api/blogs/count")
        public List<BlogReview> getMoreBlogs2(@RequestParam("id") String id) throws IOException {

            TravelPost travelPost = travelPostService.getTravelPostBycontentId(id);

            return blogReviewService.getsumBlogReview(travelPost);
        }
    }

}
