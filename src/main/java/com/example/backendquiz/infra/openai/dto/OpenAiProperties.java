package com.example.backendquiz.infra.openai.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ConfigurationProperties(prefix = "openai")
@Component
public class OpenAiProperties {

    private String apikey;
    private String url;
    private String model;
    private int countPerCategory = 3;
}
