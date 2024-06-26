package com.lec.spring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;


@Service
public class DataServiceImpl implements DataService {

    private ObjectMapper objectMapper;

    @Autowired
    public DataServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonNode fetchApiData(String url) throws IOException {

        JsonNode itemsNode = null;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // GET 요청 생성
            HttpGet request = new HttpGet(url);

            // 응답 받기
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                HttpEntity entity = response.getEntity();

                // 응답 확인

                String responseBody = EntityUtils.toString(entity);
                System.out.println("Response Body: " + responseBody);

                JsonNode rootNode = objectMapper.readTree(responseBody);
                itemsNode = rootNode.path("response").path("body").path("items").path("item");

                if (itemsNode.isMissingNode() || !itemsNode.isArray()) {
                    System.err.println("Error: 'item' node is missing or is not an array in the API response.");
                    return null;
                }

            } catch (IOException e) {
                // 예외 처리
                e.printStackTrace();
                System.out.println("An error occurred while fetching the response.");
            }

        }
        return itemsNode;
    }

    public JsonNode naverBlogReview(String responseBody) throws JsonProcessingException {

        JsonNode itemsNode = null;

        JsonNode rootNode = objectMapper.readTree(responseBody);
        itemsNode = rootNode.path("items");

        if (itemsNode.isMissingNode() || !itemsNode.isArray()) {
            System.err.println("Error: 'item' node is missing or is not an array in the API response.");
            return null;
        }

        return itemsNode;

    }


    public String fetchApiData(String url, Map<String, String> requestHeaders) throws IOException, URISyntaxException{
        HttpURLConnection con = connect(url);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 오류 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    public HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    public static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);

        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는 데 실패했습니다.", e);
        }
    }

}
