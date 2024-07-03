package com.lec.spring.domain;

import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PasswordValidator implements Validator {


    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        String inputPassword = user.getPassword();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();
        String userPassword = userDetails.getPassword();

        /**
         * 비밀번호 관련
         */
        if(!passwordEncoder.matches(inputPassword, userPassword)) {
            errors.rejectValue("password", "비밀번호가 일치하지 않습니다.");
        }


    }
}
