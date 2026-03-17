package com.example.backendquiz.infra.openai.dto;

import com.example.backendquiz.common.CategoryStatus;
import com.example.backendquiz.domain.question.Question;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OpenAiCreateQuestionQueueResponse {

    private Queue<OpenAiCreateQuestionHashMapResponse> generatedQuestions = new ArrayDeque<>();

    public void addData(OpenAiCreateQuestionHashMapResponse list) {
        this.generatedQuestions.add(list);
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OpenAiCreateQuestionHashMapResponse {

        private Map<CategoryStatus, List<OpenAiCreateQuestionResponse>> createQuestionList;

        public Map<CategoryStatus, List<OpenAiCreateQuestionResponse>> createListToHashMap(List<OpenAiCreateQuestionResponse> list) {

            CategoryStatus status = list.stream()
                    .map(OpenAiCreateQuestionResponse::getCategory)
                    .findFirst()
                    .orElseThrow();

            return Map.of(status, list);
        }

        public OpenAiCreateQuestionHashMapResponse(List<OpenAiCreateQuestionResponse> list) {
            this.createQuestionList = this.createListToHashMap(list);
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OpenAiCreateQuestionResponse {

        private CategoryStatus category;
        private String question;
        private String option1;
        private String option2;
        private String option3;
        private String option4;
        private int answer;
        private String explanation;

        public static OpenAiCreateQuestionResponse from(Question question) {
            return new OpenAiCreateQuestionResponse(
                    question.getCategory().getName(),
                    question.getQuestion(),
                    question.getOption1(),
                    question.getOption2(),
                    question.getOption3(),
                    question.getOption4(),
                    question.getAnswer(),
                    question.getExplanation()
            );
        }

    }
}
