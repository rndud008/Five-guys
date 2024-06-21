package com.lec.spring.controller;

import com.lec.spring.domain.TravelClassDetail;
import com.lec.spring.domain.TravelPost;
import com.lec.spring.domain.TravelType;
import com.lec.spring.service.BlogReviewServiceImpl;
import com.lec.spring.service.TravelPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

@Controller
public class HomeController {

    @Autowired
    private TravelPostService travelPostService;
    private BlogReviewServiceImpl blogReviewService;

    @GetMapping("/")
    public String home() {
        System.out.println("[실행확인]: home.html");
        return "home";
    }

    @GetMapping("/post/{id}")
    public String post(@PathVariable String id, Model model) throws IOException {
        model.addAttribute("post", travelPostService.getTravelPostById(id));
        System.out.println("post 결과:"+travelPostService.getTravelPostById(id));
        return "post";
    }

    @GetMapping("/festival/{id}") //495
    public String festival(@PathVariable String id, Model model) throws IOException {
        model.addAttribute("post", travelPostService.getTravelPostById(id));
        System.out.println("post 결과:"+travelPostService.getTravelPostById(id));
        return "festival";
    }


    @GetMapping("/nav")     // detail/글의 ID
    public String navbar(){
        return "navbar";      // board 밑에 있는 detail.html(뷰) 리턴
    }


}
