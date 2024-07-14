package com.lec.spring.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

    @ModelAttribute
    public void addAttributes(HttpServletRequest request, Model model) {
        String currentUrl = request.getRequestURI();
        model.addAttribute("currentUrl", currentUrl);
    }

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
