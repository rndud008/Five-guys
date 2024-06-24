package com.lec.spring.controller;

import com.lec.spring.domain.BlogReview;
import com.lec.spring.domain.TravelClassDetail;
import com.lec.spring.domain.TravelPost;
import com.lec.spring.domain.TravelType;
import com.lec.spring.service.BlogReviewService;
import com.lec.spring.service.BlogReviewServiceImpl;
import com.lec.spring.service.TravelPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private TravelPostService travelPostService;
    @Autowired
    private BlogReviewService blogReviewService;

    @GetMapping("/")
    public String home() {
        System.out.println("[실행확인]: home.html");
        return "home";
    }

    @GetMapping("/post/{id}")
    public String post(@PathVariable String id, Model model) throws IOException {

        TravelPost travelPost = travelPostService.getTravelPostById(id);
        model.addAttribute("post", travelPost);

        List<BlogReview> blogReviewList = blogReviewService.selectedTravelPostByBlogReview(travelPost);

        model.addAttribute("blogReview",blogReviewList);

        System.out.println("post 결과:"+travelPostService.getTravelPostById(id));
        return "post";
    }

    @GetMapping("/festival/{id}") //495
    public String festival(@PathVariable String id, Model model) throws IOException {

        TravelPost travelPost = travelPostService.getTravelPostById(id);
        model.addAttribute("post", travelPost);

        List<BlogReview> blogReviewList = blogReviewService.selectedTravelPostByBlogReview(travelPost);
        model.addAttribute("blogReview",blogReviewList);

        System.out.println("post 결과:"+travelPostService.getTravelPostById(id));

        System.out.println("post 결과:"+travelPostService.getTravelPostById(id));

        return "festival";
    }


    @GetMapping("/nav")     // detail/글의 ID
    public String navbar(){
        return "navbar";      // board 밑에 있는 detail.html(뷰) 리턴
    }


    @GetMapping("/fragment/navbar")
    public void nvabar(){}
}
