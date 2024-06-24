package com.lec.spring.config;

import com.lec.spring.domain.Authority;
import com.lec.spring.domain.User;
import com.lec.spring.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrincipalDetails implements UserDetails {

    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    // 로그인한 사용자 정보
    private User user;
    public User getUser() {
        return this.user;
    }

    // 일반 로그인용 생성자
    public PrincipalDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        List<Authority> list = userService.selectAuthoritiesById(user.getId()); // DB 에서 user 의 권한(들) 읽어오기

        for (Authority auth : list) {
            authorities.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return auth.getName();
                }
            });
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // OAuth 로그인용 생성자
    // todo

}
