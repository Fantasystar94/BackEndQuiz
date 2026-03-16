package com.example.backendquiz.infra.openai;

import com.example.backendquiz.domain.category.Category;
import com.example.backendquiz.domain.category.CategoryRepository;
import com.example.backendquiz.domain.question.Question;
import com.example.backendquiz.domain.question.QuestionRepository;
import com.example.backendquiz.infra.openai.dto.OpenAiProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionGeneratorService {

    private final OpenAiClient openAiClient;
    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;
    private final OpenAiProperties openAiProperties;

    @Transactional
    public void generateQuestionsForAllCategories() {
        //항상 4개
        List<Category> categoryList = categoryRepository.findAll();

        for (Category category : categoryList) {
            try {
                log.info("[QuestionGenerator] 카테고리 '{}' 문제 생성 시작", category.getName());
                generateQuestions(category);
                log.info("[QuestionGenerator] 카테고리 '{}' 문제 생성 완료", category.getName());
            } catch (Exception e) {
                log.error("[QuestionGenerator] 카테고리 '{}' 문제 생성 실패: {}", category.getName(), e.getMessage(), e);
                // 한 카테고리 실패해도 나머지 계속 진행
            }
        }

    }

    @Transactional
    public void generateQuestions(Category category) throws Exception {
        String prompt = buildPrompt(category);
        String rawJson = openAiClient.requestCompletion(prompt);


        log.debug("[QuestionGenerator] GPT 응답 raw: {}", rawJson);

        JsonNode questionNode = objectMapper.readTree(rawJson).path("questions");

        for (JsonNode node : questionNode) {
            try {
                Question question = new Question(
                        category,
                        node.path("question").asText(),
                        node.path("option1").asText(),
                        node.path("option2").asText(),
                        node.path("option3").asText(),
                        node.path("option4").asText(),
                        node.path("answer").asInt(),
                        node.path("explanation").asText()
                );
                questionRepository.save(question);
            } catch (Exception e) {
                log.warn("[QuestionGenerator] 문제 파싱/저장 실패 (스킵): {}", e.getMessage());
            }
        }

    }

    private String buildPrompt(Category category) {
        return String.format("""
                Generate %d multiple-choice quiz questions about the topic: "%s".
                Description: %s
                
                Requirements:
                - Each question must have exactly 4 options (option1 ~ option4)
                - The answer field must be an integer (1, 2, 3, or 4) indicating the correct option
                - Write all questions and options in Korean
                - Questions should be backend developer level
                - No duplicate questions
                
                Respond ONLY with this exact JSON format (no markdown, no extra text):
                {
                  "questions": [
                    {
                      "question": "질문 내용",
                      "option1": "보기 1",
                      "option2": "보기 2",
                      "option3": "보기 3",
                      "option4": "보기 4",
                      "answer": 1,
                      "explanation": "정답 해설"
                    }
                  ]
                }
                """,
                openAiProperties.getCountPerCategory(),
                category.getName(),
                category.getDescription()
        );
    }

}
