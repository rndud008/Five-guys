package com.lec.spring.service;

import com.lec.spring.domain.User;

public interface UserService {
    User findByUsername(String username);

    boolean isExist(String username);

    int register(User user);


}
