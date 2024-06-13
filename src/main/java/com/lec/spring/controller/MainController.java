package com.lec.spring.controller;

import com.lec.spring.service.SigungucodeService;
import com.lec.spring.service.TravelClassDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MainController {
    private final SigungucodeService sigungucodeService;
    private final TravelClassDetailService travelClassDetailService;


    public MainController(SigungucodeService sigungucodeService, TravelClassDetailService travelClassDetailService) {
        this.sigungucodeService = sigungucodeService;
        this.travelClassDetailService = travelClassDetailService;
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

    @GetMapping("/save2")
    public String Save2() {
        try {
            travelClassDetailService.saveTravelClassDetail();
            return "Data fetched and saved successfully!";
        } catch (IOException e) {
            return "Error occurred: " + e.getMessage();
        }
    }


}
