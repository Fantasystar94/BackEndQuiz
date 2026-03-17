package com.example.backendquiz.infra.openai;

import com.example.backendquiz.infra.openai.dto.OpenAiCreateQuestionQueueResponse;
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
    public ResponseEntity<OpenAiCreateQuestionQueueResponse> generateQuestions() {
        OpenAiCreateQuestionQueueResponse response = questionGeneratorService.generateQuestionsForAllCategories();
        return ResponseEntity.ok(response);
    }
}
