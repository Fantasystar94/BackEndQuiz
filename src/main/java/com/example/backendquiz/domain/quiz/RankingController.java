package com.example.backendquiz.domain.quiz;

import com.example.backendquiz.domain.quiz.dto.RankingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RankingController {

    private final QuizAttemptService quizAttemptService;

    @GetMapping("/ranking")
    public ResponseEntity<List<RankingResponse>> getTop10Ranking() {
        return ResponseEntity.ok(quizAttemptService.getTop10Ranking());
    }
}
