package com.lec.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/travelkorea")
    public String home() {
        System.out.println("[실행확인]: home.html");

        return "home";
    }

    @GetMapping("/fragment/navbar")
    public String navbar() {
        return "/fragment/navbar";
    }
}
