package com.example.backendquiz.domain.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuizSubmitResponse {

    private Long questionId;
    private int answer;
    private String explanation;
    private boolean correct;

}
