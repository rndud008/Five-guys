package com.lec.spring.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public CustomLoginSuccessHandler(String defaultTargetUrl) {
        // 로그인 후 redirect 할 URL 이 특별히 없는 경우 default 로 redirect 할 URL 설정
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override  // Authentication 은 로그인한 정보 객체
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        PrincipalDetails userDetails = (PrincipalDetails)authentication.getPrincipal();  // UserDetail 을 담고 있음

        List<String> roleNames = new ArrayList<>();   // 권한이름들
        authentication.getAuthorities().forEach(authority -> {
            roleNames.add(authority.getAuthority());
        });

        // 로그인 직전 Url 로 redirect 하기
        super.onAuthenticationSuccess(request, response, authentication);

    }

    // request 를 한 client ip 가져오기
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}