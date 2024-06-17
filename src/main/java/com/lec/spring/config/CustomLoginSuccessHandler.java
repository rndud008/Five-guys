package com.lec.spring.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public CustomLoginSuccessHandler(String defaultTargetUrl) {
        // 로그인 후 redirect 할 URL 이 특별히 없는 경우 default 로 redirect 할 URL 설정
        setDefaultTargetUrl(defaultTargetUrl);
    }

    // request 한 client 의 ip 가져오기
    // todo ( 필요한가?)


}