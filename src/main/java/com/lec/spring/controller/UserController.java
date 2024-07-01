package com.lec.spring.controller;

import com.lec.spring.domain.User;
import com.lec.spring.domain.UserValidator;
import com.lec.spring.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/fragment")
public class UserController {

    @Autowired
    private static UserService userService;
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
}
