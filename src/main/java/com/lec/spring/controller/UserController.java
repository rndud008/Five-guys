package com.lec.spring.controller;

import com.lec.spring.domain.User;
import com.lec.spring.domain.UserValidator;
import com.lec.spring.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private static UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/register")
    public String register(Model model) {
        return "/user/register";
    }

    @PostMapping("/register")
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

            List<FieldError> errList = result.getFieldErrors();
            for (FieldError err : errList) {
                redirectAttrs.addFlashAttribute("error", err.getCode());
            }

            return "redirect:/user/register";
        }
        // 에러 없었으면 회원 등록 진행
        String page = "/user/registerOk";
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

    @GetMapping("/login")
    public void login() {
    }

    @GetMapping("/delete")
    public String delete(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        return "user/delete";
    }


    @PostMapping("/delete")
    public String deleteOk(@RequestParam("password") String password, HttpServletRequest request, Model model) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("username:" + username);
        User user = userService.findByUsername(username);
        System.out.println("password:" + password);
        System.out.println("password:" + passwordEncoder.encode(password));
        System.out.println("password:" + user.getPassword());
        int result = 0;
        if (passwordEncoder.matches(password, user.getPassword())) {
            result = userService.deleteUser(user);
        }


        if (result > 0) {
            // redirect 시 계정 정보 session 에서 삭제.
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
            return "redirect:/travelkorea";
        } else {
            System.out.println("비밀번호가 맞지 않습니다");
            return "/user/delete";
        }
    }

    // onAuthenticationFailure 에서 로그인 실패시 forwarding 용
    // request 에 담겨진 attribute 는 Thymeleaf 에서 그대로 표현 가능.
    @PostMapping("/loginError")
    public String loginError() {
        return "home";
    }

    @GetMapping("/updateUser")
    public String update(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String name = userService.findByUsername(username).getName();
        model.addAttribute("username", username);
        model.addAttribute("name", name);
        return "user/updateUser";
    }

    @PostMapping("/updateUser")
    public String updateOk(
            @RequestParam("newpassword") String password,
            @RequestParam("email_id") String emailId,
            @RequestParam("domain") String domain,
            @RequestParam(value = "custom_domain", required = false) String customDomain,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {


//        // 검증 에러가 있을 경우 redirect 한다
//        if (result.hasErrors()) {
//
//            List<FieldError> errList = result.getFieldErrors();
//            for (FieldError err : errList) {
//                redirectAttrs.addFlashAttribute("error", err.getCode());
//            }
//
//            return "redirect:/user/updateUser";
//        }
        // 에러 없었으면 회원 등록 진행
        String username = userDetails.getUsername();
        User user = userService.findByUsername(username);
        String email;
        if (domain.equals("custom")) { email = emailId + "@" + customDomain; }
        else { email = emailId + "@" + domain; }
        System.out.println(user.getUsername());
        System.out.println(password);
        System.out.println(email);
        String page = "/user/updateOk";
        int cnt = userService.updateUser(user, passwordEncoder.encode(password), email);
        model.addAttribute("result", cnt);
        return page;
    }

    @RequestMapping("/rejectAuth")
    public String rejectAuth() {
        return "common/rejectAuth";
    }
}


