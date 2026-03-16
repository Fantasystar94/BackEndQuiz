package com.example.backendquiz.infra.openai;

import com.example.backendquiz.infra.openai.dto.OpenAiProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class OpenAiClient {

    private final OpenAiProperties openAiProperties;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;


    @PostConstruct
    public void getProperties() {
      log.info("apikey:{}",openAiProperties.getApikey());
        log.info("url:{}",openAiProperties.getUrl());
        log.info("model:{}",openAiProperties.getModel());
    }

    public String requestCompletion(String prompt) {
        Map<String, Object> requestBody = Map.of(
                "model",openAiProperties.getModel(),
                "messages", List.of(
                        Map.of("role","system","content","You are a backend quiz question generator. Always respond with valid JSON only, no markdown, no explanation."),
                        Map.of("role","user","content",prompt)
                ),
                "temperature", 0.7

        );

        try {
            String response = webClient.post()
                    .uri(openAiProperties.getUrl())
                    .header("Authorization","Bearer "+ openAiProperties.getApikey())
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode root = objectMapper.readTree(response);
            return root.path("choices").get(0).path("message").path("content").asText();

        } catch (Exception e) {
            log.error("[OpenAiClient] GPT API 호출 실패: {}", e.getMessage(), e);
            throw new RuntimeException("GPT API 호출 실패", e);
        }
    }

}
