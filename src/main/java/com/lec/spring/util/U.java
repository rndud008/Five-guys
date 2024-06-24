package com.lec.spring.util;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class U {

    // 현재 로그인 한 User
    public static User getLoggedUser() {
        PrincipalDetails userDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        return user;
    }

    // 현재 request
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attrs.getRequest();
    }

    // 현재 session
    public static HttpSession getSession() { return getRequest().getSession(); }


}
