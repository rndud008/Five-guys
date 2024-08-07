package com.lec.spring.controller;

import com.lec.spring.service.BlogReviewService;
import com.lec.spring.service.SigungucodeService;
import com.lec.spring.service.TravelClassDetailService;
import com.lec.spring.service.TravelPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class SaveController {
    @Autowired
    private SigungucodeService sigungucodeService;
    @Autowired
    private TravelClassDetailService travelClassDetailService;
    @Autowired
    private TravelPostService travelPostService;
    @Autowired
    private BlogReviewService blogReviewService;

    @GetMapping("/save")
    public String fetchAndSave() {

        try {
            sigungucodeService.saveSigungucodes();

            travelClassDetailService.saveTravelClassDetails();

            return "saved successfully!";
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

    @GetMapping("/travelPostUpdate")
    public String apiErrorTravelPostUpdate() throws IOException, URISyntaxException {
        travelPostService.updateApiErrorTravelPosts();

        return "travelPostServiceData travelPostUpdate successfully!";
    }

    @GetMapping("/travelPostModfied")
    public String apiErrorTravelPostModfied() throws IOException, URISyntaxException {
        travelPostService.modifiedtimeTravelPosts();

        return "travelPostServiceData travelPostUpdate successfully!";
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
