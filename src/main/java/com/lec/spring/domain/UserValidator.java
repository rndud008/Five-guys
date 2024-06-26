package com.lec.spring.domain;

import com.lec.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private static final String password_regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";

    @Autowired
    UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        String username = user.getUsername();
        String password = user.getPassword();




        if(username == null || username.trim().isEmpty()) { // username 은 필수
            errors.rejectValue("username", "아이디는 필수입니다.");
        } else if(userService.isExist(username)) {
            errors.rejectValue("username", "이미 존재하는 아이디 입니다");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name 은 필수입니다.");

        if(!password.matches(password_regex)) {
            errors.rejectValue("password", "비밀번호 형식이 맞지 않습니다.");
        }

        if(!user.getPassword().equals(user.getRe_password())) {
            errors.rejectValue("re_password", "비밀번호가 틀립니다.");
        }
    }
}
