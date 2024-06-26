package com.lec.spring.controller;

import com.lec.spring.domain.BlogReview;
import com.lec.spring.domain.TravelClassDetail;
import com.lec.spring.domain.TravelPost;
import com.lec.spring.domain.TravelType;
import com.lec.spring.service.BlogReviewService;
import com.lec.spring.service.BlogReviewServiceImpl;
import com.lec.spring.service.TravelPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public String post(@PathVariable String id, Model model, @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        if(userDetails != null){
            model.addAttribute("loggedUser", userDetails);
        }else {
            model.addAttribute("loggedUser", null);
        }

        TravelPost travelPost = travelPostService.getTravelPostById(id);
        travelPost.setHomepage(extraUrl(travelPost.getHomepage()));
        System.out.println(travelPost.getHomepage());
        model.addAttribute("post", travelPost);

        List<BlogReview> blogReviewList = blogReviewService.selectedTravelPostByBlogReview(travelPost);

        model.addAttribute("blogReview",blogReviewList);

        return "post";
    }

    @GetMapping("/festival/{id}") //495
    public String festival(@PathVariable String id, Model model, @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        if(userDetails != null){
            model.addAttribute("loggedUser", userDetails);
        }else {
            model.addAttribute("loggedUser", null);
        }

        TravelPost travelPost = travelPostService.getTravelPostById(id);
        travelPost.setHomepage(extraUrl(travelPost.getHomepage()));
        model.addAttribute("post", travelPost);


        List<BlogReview> blogReviewList = blogReviewService.selectedTravelPostByBlogReview(travelPost);
        model.addAttribute("blogReview",blogReviewList);

        return "festival";
    }


    @GetMapping("/nav")     // detail/글의 ID
    public String navbar(){
        return "navbar";      // board 밑에 있는 detail.html(뷰) 리턴
    }


    @GetMapping("/fragment/navbar")
    public void nvabar(){}

    public String extraUrl(String homepage){
        if(homepage == null || homepage.isEmpty()){
            return "";
        }
        int firstQuote = homepage.indexOf('"');
        int secondQuote = homepage.indexOf('"',firstQuote +1);

        if(firstQuote != -1 && secondQuote != -1){
            return homepage.substring(firstQuote+1, secondQuote);
        }
        return "";
    }
}
