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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class KTApplicationTests {
    // MySQL 접속 정보
    private static final String DB_URL = "jdbc:mysql://localhost:3306/travel?useSSL=false&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "kt326";
    private static final String DB_PASSWORD = "1234";

    public static void main(String[] args) throws IOException, SQLException {
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

        /* JSON 데이터 파싱 */
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());

            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");
            JSONObject items = (JSONObject) body.get("items");

            JSONArray itemArray = (JSONArray) items.get("item");

            // MySQL 연결
            Connection mysqlConn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // SQL 쿼리 준비
            String sql = "INSERT INTO weather_forecast (category, forecast_date, forecast_time, forecast_value, url) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = mysqlConn.prepareStatement(sql);

            for (int i = 0; i < itemArray.size(); i++) {
                JSONObject data = (JSONObject) itemArray.get(i);

                // 예보시각이 0600 또는 1500일 때 데이터베이스에 저장
                String fcstTime = (String) data.get("fcstTime");
                if (("0600".equals(fcstTime) || "1500".equals(fcstTime))) {
                    String category = (String) data.get("category");
                    String fcstDate = (String) data.get("fcstDate");
                    String fcstValue = (String) data.get("fcstValue");

                    // 파라미터 설정
                    pstmt.setString(1, category);
                    pstmt.setString(2, fcstDate);
                    pstmt.setString(3, fcstTime);
                    pstmt.setString(4, fcstValue);
                    pstmt.setString(5, url); // URL 파라미터 설정

                    // 쿼리 실행
                    pstmt.executeUpdate();
                }
            }

            // 자원 해제
            pstmt.close();
            mysqlConn.close();

        } catch (ParseException | SQLException e) {
            e.printStackTrace();
        }
    }
}
