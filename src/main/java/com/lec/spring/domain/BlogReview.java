package com.lec.spring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogReview {
    private Long id;
    private TravelPost travelPost;
    private LastCallApiDate lastCallApiData;
    private String title;
    private String link;
    private String description;
    private String postdate;
}
