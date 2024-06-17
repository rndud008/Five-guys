package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;  // 회원 아이디

    @JsonIgnore
    private String password;  // 회원 비밀번호

    private String name; // 별명

    @ToString.Exclude  // toString() 에서 제외
    @JsonIgnore
    private String re_password;  // 비밀번호 확인 입력 데이터 저장x 폼 검증을 위함

    private String email;  // 이메일

    @JsonIgnore
    private LocalDateTime regDate;

    // OAuth2 Client
    private String provide;
    private String provideId;

}
