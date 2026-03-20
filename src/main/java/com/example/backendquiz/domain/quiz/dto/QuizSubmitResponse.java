package com.example.backendquiz.domain.quiz.dto;

import com.example.backendquiz.common.CategoryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuizSubmitResponse {

    private Long questionId;
    private CategoryStatus category;
    private int answer;
    private String explanation;
    private boolean correct;

}
