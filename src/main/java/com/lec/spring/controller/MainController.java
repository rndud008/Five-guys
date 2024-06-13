package com.lec.spring.controller;

import com.lec.spring.service.SigungucodeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MainController {
    private final SigungucodeService sigungucodeService;


    public MainController(SigungucodeService sigungucodeService) {
        this.sigungucodeService = sigungucodeService;
    }

    @GetMapping("/save")
    public String fetchAndSave() {
        try {
            sigungucodeService.saveSigungucodes();
            return "Data fetched and saved successfully!";
        } catch (IOException e) {
            return "Error occurred: " + e.getMessage();
        }
    }


}
