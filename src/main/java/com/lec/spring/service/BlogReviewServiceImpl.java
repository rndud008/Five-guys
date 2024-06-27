package com.lec.spring.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.lec.spring.domain.BlogReview;
import com.lec.spring.domain.LastCallApiDate;
import com.lec.spring.domain.TravelPost;
import com.lec.spring.repository.BlogReviewRepository;
import com.lec.spring.repository.LastCallApiDateRepository;
import com.lec.spring.repository.TravelPostRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class BlogReviewServiceImpl implements BlogReviewService {

    private TravelPostRepository travelPostRepository;
    private LastCallApiDateRepository lastCallApiDataRepository;
    private BlogReviewRepository blogReviewRepository;
    private BlogReviewTransacionService blogReviewTransacionService;
    private DataService dataService;
    @Value("${app.clientId}")
    private String clientId;
    @Value("${app.clientSecret}")
    private String clientSecret;

    @Autowired
    public BlogReviewServiceImpl(SqlSession sqlSession, DataService dataService, BlogReviewTransacionService blogReviewTransacionService){
        travelPostRepository = sqlSession.getMapper(TravelPostRepository.class);
        lastCallApiDataRepository = sqlSession.getMapper(LastCallApiDateRepository.class);
        blogReviewRepository = sqlSession.getMapper(BlogReviewRepository.class);
        this.dataService = dataService;
        this.blogReviewTransacionService = blogReviewTransacionService;
    }

    @Override
    public void blogReviewSaves() throws IOException, URISyntaxException {

        List<TravelPost> travelPostList = travelPostRepository.findAll();
        JsonNode items = null;

        for (TravelPost travelPost : travelPostList){

            String text = null;
            try {
                text = URLEncoder.encode(travelPost.getTitle(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("검색어 인코딩 실패", e);
            }

            String apiURL = "https://openapi.naver.com/v1/search/blog?query="+ text + "&display=100&start=1&sort=sim";

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = today.format(formatter);

            boolean urlCheck = lastCallApiDataRepository.findByUrlAndRegDate(apiURL,formattedDate) == null;

            if (urlCheck){
                Map<String, String> requestHeaers = new HashMap<>();
                requestHeaers.put("X-Naver-Client-Id", clientId);
                requestHeaers.put("X-Naver-Client-Secret", clientSecret);
                String responseBody = dataService.fetchApiData(apiURL, requestHeaers);

                items = dataService.naverBlogReview(responseBody);

                System.out.println(responseBody);

                if (items != null){
                    LastCallApiDate lastCallApiData = new LastCallApiDate();
                    lastCallApiData.setUrl(apiURL);
                    lastCallApiDataRepository.save(lastCallApiData);
                    for (JsonNode item : items){

                        boolean linkCheck = blogReviewRepository.findByLinkAndTravelPostId(item.get("link").asText(),travelPost.getId()) == null;

                        if(linkCheck){
                            blogReviewTransacionService.itemSave(item, travelPost, lastCallApiData);
                        }else {
                            System.out.println("이미 저장 되어 있음.");
                            System.out.println(item.get("link").asText());
                        }

                    }
                    // url set.
                }else {
                    System.out.println(" 검색 결과 없음.");
                }
            }else {
                System.out.println(" 오늘 이미 호출함.");
                System.out.println(travelPost.getId());
            }

            timeUnit();

        }

    }

    @Override
    public List<BlogReview> selectedTravelPostByBlogReview(TravelPost travelPost, int offset, int limit) {

        return blogReviewRepository.findTravelPostByBlogReview(travelPost, offset, limit);
    }




    public void timeUnit() {
        // API 호출 간격을 두기 위해 잠시 대기
        try {
            TimeUnit.MILLISECONDS.sleep(500);
            System.out.println("timeUnit 실행");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


}
