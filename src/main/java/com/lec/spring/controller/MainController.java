package com.lec.spring.controller;

import com.lec.spring.service.SigungucodeService;
import com.lec.spring.service.TravelClassDetailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class MainController {
    private SigungucodeService sigungucodeService;
    private TravelClassDetailService travelClassDetailService;


    public MainController(SigungucodeService sigungucodeService,TravelClassDetailService travelClassDetailService) {
        this.sigungucodeService = sigungucodeService;
        this.travelClassDetailService = travelClassDetailService;
    }

    @GetMapping("/save")
    public String fetchAndSave() {
        try {
            sigungucodeService.saveSigungucodes();
            return "sigungucodeServiceData fetched and saved successfully!";
        } catch (IOException e) {
            return "Error occurred: " + e.getMessage();
        }
    }

    @GetMapping("/travelType")
    public String travelTpyeFetchAndSave() {
        try {
            travelClassDetailService.saveTravelClassDetails();
            return "travelClassDetailServiceData fetched and saved successfully!";
        } catch (IOException e) {
            return "Error occurred: " + e.getMessage();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
