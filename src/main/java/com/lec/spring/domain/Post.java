package com.lec.spring.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    private Long id;
    private Long areacode_id;
    private String subject;
    private String content;
    private LocalDateTime regdate;
    private Long viewcnt;

    private User user;
    //    private Areacode areacode;
//    필요한가?
    @ToString.Exclude
    @Builder.Default
    private List<Attachment> fileList = new ArrayList<>();

}
