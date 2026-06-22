

package com.ai.springaidemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ImageService {

    @Value("${spring.ai.stabilityai.api-key}")
    private String apiKey;

    private final String URL = "https://api.stability.ai/v2beta/stable-image/generate/core";

    public List<byte[]> generateImages(String prompt, int count) {

        RestTemplate restTemplate = new RestTemplate();
        List<byte[]> images = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            // 🔹 Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON)); // ✅ FIXED

            // 🔹 Body
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("prompt", prompt);
            body.add("output_format", "png");

            HttpEntity<MultiValueMap<String, Object>> request =
                    new HttpEntity<>(body, headers);

            // 🔹 API Call
            ResponseEntity<String> response = restTemplate.exchange(
                    URL,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            // 🔹 Extract base64 image from JSON
            String responseBody = response.getBody();

            String base64Image = responseBody
                    .split("\"image\":\"")[1]
                    .split("\"")[0];

            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            images.add(imageBytes);
        }

        return images;
    }
}