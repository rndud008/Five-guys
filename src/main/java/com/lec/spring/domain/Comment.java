package com.lec.spring.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    private Long id;  // PK

    @ToString.Exclude // ToString 에서 제외
    private User user;  // 댓글 작성자 (FK)

    @JsonIgnore
    private Long travel_diary_post_id;  // 어느글의 댓글 (FK)

    private String content; // 댓글 내용

    // java.time.* 객체 변환을 위한 annotation
    // Serializer 는 Java 객체를 JSON 데이터로 변환할 때 사용되고
    // Deserializer 는 JSON 데이터를 Java 객체로 변환할 때 사용
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonProperty("regdate")  // 변환하고자 하는 형태 지정 가능
    private LocalDateTime regdate;
}
