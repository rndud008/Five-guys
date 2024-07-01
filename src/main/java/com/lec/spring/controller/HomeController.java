package com.lec.spring.controller;

import com.lec.spring.domain.*;
import com.lec.spring.service.BlogReviewService;
import com.lec.spring.service.BlogReviewServiceImpl;
import com.lec.spring.service.TravelPostService;
import com.lec.spring.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class HomeController {

    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private TravelPostService travelPostService;
    @Autowired
    private BlogReviewService blogReviewService;

    @GetMapping("/")
    public String home() {
        System.out.println("[실행확인]: home.html");
        return "home";
    }

    @GetMapping("/post/{id}")
    public String post(@PathVariable String id, Model model, @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        if(userDetails != null){
            model.addAttribute("loggedUser", userDetails);
        }else {
            model.addAttribute("loggedUser", null);
        }

        TravelPost travelPost = null;
        try {
            travelPost = travelPostService.getTravelPostBycontentId(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        travelPost = travelPostService.update(travelPost);

        travelPost.setHomepage(extraUrl(travelPost.getHomepage()));
        System.out.println(travelPost.getHomepage());
        model.addAttribute("post", travelPost);

        List<BlogReview> blogReviewList = blogReviewService.selectedTravelPostByBlogReview(travelPost, 0, 5);

        model.addAttribute("blogReview",blogReviewList);

//        System.out.println("post 결과:"+travelPostService.getTravelPostById(id));
        return "post";
    }

    @GetMapping("/festival/{id}") //495
    public String festival(@PathVariable String id, Model model, @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        if(userDetails != null){
            model.addAttribute("loggedUser", userDetails);
        }else {
            model.addAttribute("loggedUser", null);
        }

        TravelPost travelPost = travelPostService.getTravelPostBycontentId(id);

        travelPost = travelPostService.update(travelPost);

        travelPost.setHomepage(extraUrl(travelPost.getHomepage()));
        model.addAttribute("post", travelPost);


        List<BlogReview> blogReviewList = blogReviewService.selectedTravelPostByBlogReview(travelPost, 0, 5);
        model.addAttribute("blogReview",blogReviewList);

//        System.out.println("post 결과:"+travelPostService.getTravelPostById(id));
//
//        System.out.println("post 결과:"+travelPostService.getTravelPostById(id));

        return "festival";
    }


//    @GetMapping("/nav")     // detail/글의 ID
//    public String nvabar(){
//        return "navbar";      // board 밑에 있는 detail.html(뷰) 리턴
//    }

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


    public String extraUrl(String homepage){
        if(homepage == null || homepage.isEmpty()){
            return "";
        }

        Pattern pattern = Pattern.compile("\"(http[^\"]*)\"");
        Matcher matcher = pattern.matcher(homepage);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }


    @RestController
    public class BlogRestController {

        @GetMapping("/api/festival/blogs")
        public List<BlogReview> getMoreBlogs(@RequestParam("id") String id, @RequestParam("offset") int offset) throws IOException {

            TravelPost travelPost = travelPostService.getTravelPostBycontentId(id);
            System.out.println(blogReviewService.selectedTravelPostByBlogReview(travelPost, offset, 3));

            return blogReviewService.selectedTravelPostByBlogReview(travelPost, offset, 3);
        }

        @GetMapping("/api/festival/blogs/count")
        public List<BlogReview> getMoreBlogs2(@RequestParam("id") String id) throws IOException {

            TravelPost travelPost = travelPostService.getTravelPostBycontentId(id);

            return blogReviewService.getsumBlogReview(travelPost);
        }
    }
}
