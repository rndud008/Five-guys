package com.lec.spring.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 로그아웃 시간 남기기
        // todo 필요한가

        String redirectUrl = "/user/login?logoutHandler";

        // return_url 이 있는 경우 logout 후 해당 url 로 redirect.
        if(request.getParameter("ret_url")!=null){
            redirectUrl = request.getParameter("ret_url");
        }
        // 기본 성공 url 설정
//        else {
//            response.sendRedirect("/defaultSuccessUrl");
//        }
        response.sendRedirect(redirectUrl);
    }
}
