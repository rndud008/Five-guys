package com.lec.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")

public class HomeController {
    @RequestMapping("/")
    public String home() {
        return "redirect:/home";
    }
    @RequestMapping("/home")
    public void home1(){}
}
