package com.example.backendquiz.domain.quiz;

import com.example.backendquiz.auth.AuthUser;
import com.example.backendquiz.domain.category.CategoryRepository;
import com.example.backendquiz.domain.quiz.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/quiz")
@RequiredArgsConstructor
public class QuizController {
    private final CategoryRepository categoryRepository;
    private final QuizService quizService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> getCategories() {
        List<CategoryResponse> categories = categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::new)
                .toList();

        return ResponseEntity.ok(categories);

    }

    // 랜덤 문제 1개
    @GetMapping("/{categoryId}")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable Long categoryId, @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(quizService.getRandomQuestion(categoryId, authUser));
    }

    // 답 제출
    @PostMapping("/submit")
    public ResponseEntity<QuizSubmitResponse> submit(
            @RequestBody QuizSubmitRequest request,
            @AuthenticationPrincipal AuthUser authUser  // 비로그인이면 null
    ) {
        return ResponseEntity.ok(quizService.submit(request, authUser));
    }
}
