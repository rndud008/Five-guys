package com.lec.spring.controller;

import com.lec.spring.config.PrincipalDetailService;
import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.PasswordValidator;
import com.lec.spring.domain.UpdateValidator;
import com.lec.spring.domain.User;
import com.lec.spring.domain.UserValidator;
import com.lec.spring.service.UserService;
import com.lec.spring.util.U;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
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
    private PrincipalDetailService principalDetailService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @ModelAttribute
    public void addAttributes(HttpServletRequest request, Model model) {
        String currentUrl = request.getRequestURI();
        model.addAttribute("currentUrl", currentUrl);
    }


    @GetMapping("/register")
    public String register(@RequestParam(name = "redirectUrl", required = false) String redirectUrl, Model model) {
        model.addAttribute("redirectUrl",redirectUrl);

        return "user/register";
    }

    @PostMapping("/register")
    public String registerOk(
            @Valid @ModelAttribute("registerUser") User user,
            BindingResult result,
            Model model,
            @RequestParam("redirectUrl") String redirectUrl,
            RedirectAttributes redirectAttrs
    ) {

        // 검증 에러가 있을 경우 redirect 한다
        if (result.hasErrors()) {

            redirectAttrs.addFlashAttribute("username",user.getUsername());
            redirectAttrs.addFlashAttribute("name",user.getName());
            redirectAttrs.addFlashAttribute("email_id",user.getEmail().substring(0, user.getEmail().lastIndexOf('@')));
            redirectAttrs.addFlashAttribute("custom_domain",user.getEmail().substring(user.getEmail().lastIndexOf('@')+1, user.getEmail().length()));

            List<FieldError> errList = result.getFieldErrors();
            for (FieldError err : errList) {
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/user/register";
        }
        // 에러 없었으면 회원 등록 진행
        String page = "user/registerOk";
        int cnt = userService.register(user);
        model.addAttribute("result", cnt);

        user = userService.findByUsername(user.getUsername());

        UserDetails userDetails = principalDetailService.loadUserByUsername(user.getUsername());

        Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);

        HttpSession session = U.getSession();

        if(session != null){
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        }

        if (redirectUrl != null && !redirectUrl.isEmpty()) {
            return "redirect:" + redirectUrl;
        } else {
            return page;
        }
    }

    @Autowired
    UserValidator userValidator;

    @InitBinder("registerUser")
    public void initBinder(WebDataBinder binder){
        binder.setValidator(userValidator);
    }

    @GetMapping("/login")
    public void login() {

    }

    @GetMapping("/delete")
    public String delete() {
        return "user/delete";
    }


    @PostMapping("/delete")
    public String deleteOk(@RequestParam("password") String password,
                           HttpServletRequest request,
                           Model model,
                           RedirectAttributes redirectAttrs) {



        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        int result = 0;
        if (passwordEncoder.matches(password, user.getPassword())) {
            result = userService.deleteUser(user);
        }else {
            redirectAttrs.addFlashAttribute("errorMessage", "비밀번호가 맞지 않습니다.");
            return "redirect:/user/delete"; // 에러 발생 시 돌아갈 페이지
        }

        if (result > 0) {
            // redirect 시 계정 정보 session 에서 삭제.
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
            model.addAttribute("result",result);

            return "user/deleteOk";
        } else {
            System.out.println("비밀번호가 맞지 않습니다");
            return "user/delete";
        }
    }

    @PostMapping("/loginError")
    public String loginError() {
        return "home";
    }

    @GetMapping("/updateUser")
    public String update(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String name = userService.findByUsername(username).getName();

        User user = userService.findByUsername(username);

//        model.addAttribute("username", username);
//        model.addAttribute("name", name);
//        model.addAttribute("email_id", user.getEmail().substring(0,user.getEmail().lastIndexOf('@')));
//        model.addAttribute("custom_domain", user.getEmail().substring(user.getEmail().lastIndexOf('@')+1,user.getEmail().length()));

        return "user/updateUser";
    }


    @PostMapping("/updateUser")
    public String updateOk(
            @Valid @ModelAttribute("updateUser") User user,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttrs
    ) {

        // 검증 에러가 있을 경우 redirect 한다
        if (result.hasErrors()) {

            List<FieldError> errList = result.getFieldErrors();
            for (FieldError err : errList) {
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/user/updateUser";
        }

        String page = "user/updateOk";
        int cnt = userService.updateUser(user, passwordEncoder.encode(user.getPassword()), user.getEmail());
        model.addAttribute("result", cnt);

        user = userService.findByUsername(user.getUsername());

        UserDetails userDetails = principalDetailService.loadUserByUsername(user.getUsername());

        Authentication newAuth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(newAuth);

        HttpSession session = U.getSession();

        if(session != null){
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        }

        return page;
    }
    @Autowired
    UpdateValidator updateValidator;

    @InitBinder("updateUser")
    public void upinitBinder(WebDataBinder binder){
        binder.setValidator(updateValidator);
    }

    @GetMapping("/updateCheckUser")
    public String updateCheckUser() {
        return "user/updateCheckUser";
    }

    @PostMapping("/updateCheckUser")
    public String updateCheckUserOk(
            @Valid @ModelAttribute("passwordUser") User user,
            BindingResult result,
            RedirectAttributes redirectAttrs
    ) {
        // 검증 에러가 있을 경우 redirect 한다
        if (result.hasErrors()) {

            List<FieldError> errList = result.getFieldErrors();
            for (FieldError err : errList) {
                redirectAttrs.addFlashAttribute("error_" + err.getField(), err.getCode());
            }

            return "redirect:/user/updateCheckUser";
        } else {
            return "user/updateUser";
        }
    }

    @Autowired
    PasswordValidator passwordValidator;

    @InitBinder("passwordUser")
    public void pinitBinder(WebDataBinder binder){
        binder.setValidator(passwordValidator);
    }

    @RequestMapping("/rejectAuth")
    public String rejectAuth() {
        return "common/rejectAuth";
    }


}


