package com.lec.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    private Long id;
    private String subject;
    private String content;
    private LocalDateTime regdate;
    private Long viewcnt;

    private User user;
    private Areacode areacode;


}
