package com.example.backendquiz.domain.quizattempt;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/quizAttempt")
@RequiredArgsConstructor
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    @PostMapping("/solve")
    public boolean solveQuestion() {

        return quizAttemptService.solveQuestion(
                1L,
                1L,
                2
        );
    }
}
