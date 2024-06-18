package com.lec.spring.controller;

import com.lec.spring.service.BlogReviewService;
import com.lec.spring.service.SigungucodeService;
import com.lec.spring.service.TravelClassDetailService;
import com.lec.spring.service.TravelPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class MainController {
    @Autowired
    private SigungucodeService sigungucodeService;
    @Autowired
    private TravelClassDetailService travelClassDetailService;
    @Autowired
    private TravelPostService travelPostService;
    @Autowired
    private BlogReviewService blogReviewService;

    /*
    public MainController(SigungucodeService sigungucodeService,TravelClassDetailService travelClassDetailService) {
        this.sigungucodeService = sigungucodeService;
        this.travelClassDetailService = travelClassDetailService;

    }
    */


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
    public String travelTypeFetchAndSave() {
        try {
            travelClassDetailService.saveTravelClassDetails();
            return "travelClassDetailServiceData fetched and saved successfully!";
        } catch (IOException e) {
            return "Error occurred: " + e.getMessage();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/travelPost")
    public String travelPostFetchAndSave(){
        try {
            travelPostService.saveTravelPosts();
            return "travelPostServiceData fetched and saved successfully!";
        } catch (IOException e) {
            return "Error occurred: " + e.getMessage();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/travelBlog")
    public String travelBlogFetchAndSave(){
        try {
            blogReviewService.blogReviewSaves();
            return "travelBlogServiceData fetched and saved successfully!";
        } catch (IOException e) {
            return "Error occurred: " + e.getMessage();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
