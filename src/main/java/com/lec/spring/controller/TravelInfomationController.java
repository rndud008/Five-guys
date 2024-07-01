package com.lec.spring.controller;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.BlogReview;
import com.lec.spring.domain.TravelPost;
import com.lec.spring.domain.User;
import com.lec.spring.service.BlogReviewService;
import com.lec.spring.service.TravelPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/categoryTable")
public class TravelInfomationController {
//
//    @Autowired
//    TravelPostService travelPostService;
//    @Autowired
//    BlogReviewService blogReviewService;
//
//    @GetMapping("/12/post/{contentId}")
//    public String post(@PathVariable String contentId, Model model, @AuthenticationPrincipal UserDetails userDetails) throws IOException {
//
//        if(userDetails != null){
//            User loggedUser = ((PrincipalDetails) userDetails).getUser();
//            model.addAttribute("loggedUser", loggedUser);
//        }else {
//            model.addAttribute("loggedUser", null);
//        }
//
//        TravelPost travelPost = travelPostService.getTravelPostBycontentId(contentId);
//
//        travelPost = travelPostService.update(travelPost);
//
//        travelPost.setHomepage(extraUrl(travelPost.getHomepage()));
//        System.out.println(travelPost.getHomepage());
//        model.addAttribute("post", travelPost);
//
//        List<BlogReview> blogReviewList = blogReviewService.selectedTravelPostByBlogReview(travelPost, 0, 5);
//
//        model.addAttribute("blogReview",blogReviewList);
//
//        return "post";
//
//    }
//
//    @GetMapping("/15/festival/{id}") //495
//    public String festival(@PathVariable String id, Model model, @AuthenticationPrincipal UserDetails userDetails) throws IOException {
//
//        if(userDetails != null){
//            User loggedUser = ((PrincipalDetails) userDetails).getUser();
//            model.addAttribute("loggedUser", loggedUser);
//        }else {
//            model.addAttribute("loggedUser", null);
//        }
//
//        TravelPost travelPost = travelPostService.getTravelPostBycontentId(id);
//
//        travelPost = travelPostService.update(travelPost);
//
//        travelPost.setHomepage(extraUrl(travelPost.getHomepage()));
//        model.addAttribute("post", travelPost);
//
//
//        List<BlogReview> blogReviewList = blogReviewService.selectedTravelPostByBlogReview(travelPost, 0, 5);
//        model.addAttribute("blogReview",blogReviewList);
//
//        return "festival";
//    }
//
//    public String extraUrl(String homepage){
//        if(homepage == null || homepage.isEmpty()){
//            return "";
//        }
//
//        Pattern pattern = Pattern.compile("\"(http[^\"]*)\"");
//        Matcher matcher = pattern.matcher(homepage);
//
//        if (matcher.find()) {
//            return matcher.group(1);
//        }
//
//        return "";
//    }

}
