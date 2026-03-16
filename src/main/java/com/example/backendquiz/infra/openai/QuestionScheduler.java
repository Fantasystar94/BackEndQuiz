package com.example.backendquiz.infra.openai;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuestionScheduler {

    private final QuestionGeneratorService questionGeneratorService;

    //매일 새벽3시
    @Scheduled(cron = "0 0 3 * * *")
    public void scheduledQuestionGeneration() {
        log.info("[QuestionScheduler] 문제 자동 생성 시작");
        questionGeneratorService.generateQuestionsForAllCategories();
        log.info("[QuestionScheduler] 문제 자동 생성 완료");
    }
}
