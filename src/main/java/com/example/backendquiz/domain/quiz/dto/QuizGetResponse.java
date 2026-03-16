package com.example.backendquiz.domain.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuizGetResponse {

    private Long id;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;

}
