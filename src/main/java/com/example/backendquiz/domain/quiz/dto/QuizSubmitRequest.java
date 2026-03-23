package com.example.backendquiz.domain.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuizSubmitRequest {

    private Long questionId;
    private int answer;
    private Long currentCategoryId;
}
