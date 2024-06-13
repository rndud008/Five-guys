package com.lec.spring.repository;

import com.lec.spring.domain.User;

public interface UserRepository {
    User findById(Long id);

    User findByUsername(String username);

    int save(User user);

    int update(User user);

}
