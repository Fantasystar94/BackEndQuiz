package com.example.backendquiz.infra.openai;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/questions")
@RequiredArgsConstructor
public class QuestionAdminController {

    public final QuestionGeneratorService questionGeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateQuestions() {
        questionGeneratorService.generateQuestionsForAllCategories();
        return ResponseEntity.ok("문제생성완료");
    }
}
