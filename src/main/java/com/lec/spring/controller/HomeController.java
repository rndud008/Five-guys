package com.lec.spring.controller;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.*;
import com.lec.spring.service.BlogReviewService;
import com.lec.spring.service.LastCallApiDateService;
import com.lec.spring.service.TravelPostService;
import com.lec.spring.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class HomeController {
    @Value("${app.apikey}")
    private String apikey;

    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private TravelPostService travelPostService;
    @Autowired
    private BlogReviewService blogReviewService;
    @Autowired
    private LastCallApiDateService lastCallApiDateService;

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("[실행확인]: home.html");

        if (userDetails != null) {
            User loggedUser = ((PrincipalDetails) userDetails).getUser();
            model.addAttribute("loggedUser", loggedUser);
        } else {
            model.addAttribute("loggedUser", null);
        }

        return "home";
    }

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

    /**
     * 로그인 관련 처리
     */
    @GetMapping("/fragment/navbar")
    public void navbar(){}

    @GetMapping("/fragment/register")
    public String register(Model model) {
        return "/fragment/register";
    }

    @PostMapping("/fragment/register")
    public String registerOk(
            @Valid User user,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttrs,
            @RequestParam("email_id") String emailId,
            @RequestParam("domain") String domain,
            @RequestParam(value = "custom_domain", required = false) String customDomain
    ) {
        String email;
        if (domain.equals("custom")) { email = emailId + "@" + customDomain; }
        else { email = emailId + "@" + domain; }

        user.setEmail(email);

        // 검증 에러가 있을 경우 redirect 한다
        if (result.hasErrors()) {
            redirectAttrs.addFlashAttribute("username", user.getUsername());
            redirectAttrs.addFlashAttribute("name", user.getName());
            redirectAttrs.addFlashAttribute("email", user.getEmail());

            List<FieldError> errList = result.getFieldErrors();
            for (FieldError err : errList) {
                redirectAttrs.addFlashAttribute("error", err.getCode());
            }

            return "redirect:/fragment/register";
        }
        // 에러 없었으면 회원 등록 진행
        String page = "/fragment/registerOk";
        int cnt = userService.register(user);
        model.addAttribute("result", cnt);
        return page;
    }

    @Autowired
    UserValidator userValidator;

    @InitBinder
    public void initBinder(WebDataBinder binder){
        binder.setValidator(userValidator);
    }

    @GetMapping("/fragment/login")
    public void login() {
    }

    // onAuthenticationFailure 에서 로그인 실패시 forwarding 용
    // request 에 담겨진 attribute 는 Thymeleaf 에서 그대로 표현 가능.
    @PostMapping("fragment/loginError")
    public String loginError() {
        return "fragment/login";
    }

    @RequestMapping("fragment/rejectAuth")
    public String rejectAuth() {
        return "common/rejectAuth";
    }





    @RestController
    public class BlogRestController {

        @GetMapping("/api/blogs")
        public List<BlogReview> getMoreBlogs(@RequestParam("id") String id, @RequestParam("offset") int offset) throws IOException {

            TravelPost travelPost = travelPostService.getTravelPostBycontentId(id);
            System.out.println(blogReviewService.selectedTravelPostByBlogReview(travelPost, offset, 3));

            return blogReviewService.selectedTravelPostByBlogReview(travelPost, offset, 3);
        }

        @GetMapping("/api/blogs/count")
        public List<BlogReview> getMoreBlogs2(@RequestParam("id") String id) throws IOException {

            TravelPost travelPost = travelPostService.getTravelPostBycontentId(id);

            return blogReviewService.getsumBlogReview(travelPost);
        }
    }
}
