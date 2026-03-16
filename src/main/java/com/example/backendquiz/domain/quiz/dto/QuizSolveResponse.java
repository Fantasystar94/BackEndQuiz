package com.example.backendquiz.domain.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizSolveResponse {
    private Long questionId;
    private boolean correct;
    private int correctAnswer;
    private String explanation;
}
