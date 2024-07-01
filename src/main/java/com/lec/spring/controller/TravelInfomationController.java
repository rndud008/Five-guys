package com.lec.spring.controller;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.BlogReview;
import com.lec.spring.domain.LastCallApiDate;
import com.lec.spring.domain.TravelPost;
import com.lec.spring.domain.User;
import com.lec.spring.service.BlogReviewService;
import com.lec.spring.service.LastCallApiDateService;
import com.lec.spring.service.TravelPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class TravelInfomationController {
    @Value("${app.apikey}")
    private String apikey;

    @Autowired
    private TravelPostService travelPostService;
    @Autowired
    private BlogReviewService blogReviewService;
    @Autowired
    private LastCallApiDateService lastCallApiDateService;

    @GetMapping("/post/{id}")
    public String post(@PathVariable String id, Model model, @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        if (userDetails != null) {
            User loggedUser = ((PrincipalDetails) userDetails).getUser();
            model.addAttribute("loggedUser", loggedUser);
        } else {
            model.addAttribute("loggedUser", null);
        }

        TravelPost travelPost = travelPostService.getTravelPostBycontentId(id);

        String apiUrl = String.format("https://apis.data.go.kr/B551011/KorService1/" +
                "detailCommon1?serviceKey=%s" +
                "&MobileOS=ETC&MobileApp=AppTest&_type=json&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&numOfRows=10&pageNo=1" +
                "&contentId=%s&contentTypeId=%d", apikey, travelPost.getContentid(), travelPost.getTravelClassDetail().getTravelType().getId());


        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);

        if (lastCallApiDateService.selectedByUrlAndRegDate(apiUrl, formattedDate) == null) {
            LastCallApiDate lastCallApiDate = new LastCallApiDate();
            lastCallApiDate.setUrl(apiUrl);
            lastCallApiDateService.save(lastCallApiDate);

            travelPost = travelPostService.update(travelPost,lastCallApiDate);
        }

        travelPost.setHomepage(extraUrl(travelPost.getHomepage()));
        System.out.println(travelPost.getHomepage());
        model.addAttribute("post", travelPost);

        List<BlogReview> blogReviewList = blogReviewService.selectedTravelPostByBlogReview(travelPost, 0, 5);

        model.addAttribute("blogReview", blogReviewList);

        return "post";
    }

    @GetMapping("/festival/{id}") //495
    public String festival(@PathVariable String id, Model model, @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        if (userDetails != null) {
            User loggedUser = ((PrincipalDetails) userDetails).getUser();
            model.addAttribute("loggedUser", loggedUser);
        } else {
            model.addAttribute("loggedUser", null);
        }

        TravelPost travelPost = travelPostService.getTravelPostBycontentId(id);

        String apiUrl = String.format("https://apis.data.go.kr/B551011/KorService1/" +
                "detailCommon1?serviceKey=%s" +
                "&MobileOS=ETC&MobileApp=AppTest&_type=json&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&numOfRows=10&pageNo=1" +
                "&contentId=%s&contentTypeId=%d", apikey, travelPost.getContentid(), travelPost.getTravelClassDetail().getTravelType().getId());


        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = today.format(formatter);

        if (lastCallApiDateService.selectedByUrlAndRegDate(apiUrl, formattedDate) == null) {
            LastCallApiDate lastCallApiDate = new LastCallApiDate();
            lastCallApiDate.setUrl(apiUrl);
            lastCallApiDateService.save(lastCallApiDate);

            travelPost = travelPostService.update(travelPost,lastCallApiDate);
        }

        travelPost.setHomepage(extraUrl(travelPost.getHomepage()));
        model.addAttribute("post", travelPost);


        List<BlogReview> blogReviewList = blogReviewService.selectedTravelPostByBlogReview(travelPost, 0, 5);
        model.addAttribute("blogReview", blogReviewList);


        return "festival";
    }

    public String extraUrl(String homepage) {
        if (homepage == null || homepage.isEmpty()) {
            return "";
        }

        Pattern pattern = Pattern.compile("\"(http[^\"]*)\"");
        Matcher matcher = pattern.matcher(homepage);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }


}
