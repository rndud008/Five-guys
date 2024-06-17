package com.lec.spring.service;

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

import java.io.IOException;
import java.net.URISyntaxException;


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


}
