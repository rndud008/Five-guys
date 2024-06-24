package com.lec.spring.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public CustomLoginSuccessHandler(String defaultTargetUrl) {
        // 로그인 후 redirect 할 URL 이 특별히 없는 경우 default 로 redirect 할 URL 설정
        setDefaultTargetUrl(defaultTargetUrl);
    }

//    public static String getClientIp(HttpServletRequest request) {
//
//    }

    @Override
    public void onAuthenticationSuccess
            (HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        List<String> roleNames = new ArrayList<>();
        authentication.getAuthorities().forEach(authority -> {
            roleNames.add(authority.getAuthority());
        });

        super.onAuthenticationSuccess(request, response, authentication);
    }
}