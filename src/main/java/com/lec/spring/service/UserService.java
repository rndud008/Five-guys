package com.lec.spring.service;

import com.lec.spring.domain.Authority;
import com.lec.spring.domain.User;

import java.util.List;

public interface UserService {
    User findByUsername(String username);

    boolean isExist(String username);

    int register(User user);

    List<Authority> selectAuthoritiesById(Long id);

    int deleteUser(User user);
}
