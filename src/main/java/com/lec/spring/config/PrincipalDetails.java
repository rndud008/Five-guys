package com.lec.spring.config;

import com.lec.spring.domain.Authority;
import com.lec.spring.domain.User;
import com.lec.spring.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

public class PrincipalDetails implements UserDetails, OAuth2User {

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

    // OAuth2 로그인용 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        System.out.println("""
                UserDetails(user, oauth attributes) 생성:
                    user: %s
                    attributes: %s
                """.formatted(user, attributes));
        this.user = user;
        this.attributes = attributes;
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

    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
}
