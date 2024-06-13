package com.lec.spring.repository;

import com.lec.spring.domain.Authority;
import com.lec.spring.domain.User;

import java.util.List;

public interface AuthorityRepository {
    Authority findByName(String name);

    List<Authority> findByUser(User user);

    int addAuthority(Long user_id, Long auth_id);
}
