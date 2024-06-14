package com.lec.spring;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KTApplicationTests {
    public static void main(String[] args) throws IOException {
        String baseUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
        String serviceKey = "oshjO8WG9VLp87/CQQK/YzU9KWIOr/3VlA8jNBbi40aHpZM1RyvXyDNiCfF3IMl4wPg0UicSNMFHYNtQZVfzNQ==";
        String baseTime = "0200";
        String nx = "60";
        String ny = "127";
        String numOfRows = "1000";

        // 현재 시간을 yyyyMMdd 형식으로 포맷팅
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String baseDate = dateFormat.format(new Date());

        // URL 구성 using UriComponentsBuilder
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", "1")
                .queryParam("numOfRows", numOfRows)
                .queryParam("dataType", "JSON")
                .queryParam("base_date", baseDate)
                .queryParam("base_time", baseTime)
                .queryParam("nx", nx)
                .queryParam("ny", ny)
                .toUriString();

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        System.out.println("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

//        System.out.println(sb.toString());

        /* JSON 데이터 파싱 */
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());

            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");

            JSONArray itemArray = (JSONArray) items.get("item");

            for (int i = 0; i < itemArray.size(); i++) {
                JSONObject data = (JSONObject) itemArray.get(i);
                // 카테고리 값을 디버깅 출력
                String category = (String) data.get("category");

                // 예보시각이 0500 또는 1500일 때 출력
                String fcstTime = (String) data.get("fcstTime");
                if (("0600".equals(fcstTime) || "1500".equals(fcstTime))
                        &&
                        ("tmn".equalsIgnoreCase(category) ||
                         "tmx".equalsIgnoreCase(category) ||
                         "sky".equalsIgnoreCase(category) ||
                         "pop".equalsIgnoreCase(category) ||
                         "pty".equalsIgnoreCase(category))) {

                    String fcstDate = (String) data.get("fcstDate");
                    String fcstValue = (String) data.get("fcstValue");

                    System.out.println("카테고리: " + category + ", 예보일자: " + fcstDate + ", 예보시각: " + fcstTime + ", 예보 값: " + fcstValue + "\n");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
