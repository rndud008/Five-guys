package com.lec.spring.domain;

import com.lec.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UpdateValidator implements Validator {

    private static final String username_regex = "^[a-zA-Z]{1}[a-zA-Z0-9_]{3,11}$";
    private static final String password_regex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[a-zA-Z\\d!@#$%^&*(),.?\":{}|<>]{8,12}$";
    private static final String email_regex = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";




    @Autowired
    UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        String password = user.getPassword();
        String email = user.getEmail();

        /**
         * 비밀번호 관련
         */
        if(!password.matches(password_regex)) {
            errors.rejectValue("password", "비밀번호 형식이 맞지 않습니다.");
        }

        if(!user.getPassword().equals(user.getRe_password())) {
            errors.rejectValue("re_password", "비밀번호가 일치하지 않습니다.");
        }

        /**
         * 이메일 관련
         */


        System.out.println(user.getEmail());
        System.out.println(email);

        if(!email.matches(email_regex)) {
            errors.rejectValue("email","E-mail 형식이 맞지 않습니다.");
        }

    }
}
