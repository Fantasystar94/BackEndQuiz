package com.example.backendquiz.infra.openai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OpenAiRequest {

    private String model;
    private List<Map<String, String>> messages;
}
