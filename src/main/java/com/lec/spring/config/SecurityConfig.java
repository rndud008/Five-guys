package com.lec.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())   // CSRF(Cross-site Request Forgery: 사이트 간 요청 위조)

                /**
                 * request URL 에 대한 접근 권한 세팅
                 */
                .authorizeHttpRequests(auth -> auth
                        // 그 외 request 는 모두 permit
                        .anyRequest().permitAll()
                )

                /**
                 *  폼 로그인 설정
                 */
                .formLogin(form -> form
                        .loginPage("/user/login")   // 로그인 필요한 상황 발생시 "/user/login" 으로 request 요청
                        .loginProcessingUrl("/user/login")  // "/user/login" url 로 POST request 가 들어오면 Security 가 fetch 하여 처리. "인증" 과정
                        .defaultSuccessUrl("/")
                        //todo
                        .permitAll()
                )
                .build();
    }
}
