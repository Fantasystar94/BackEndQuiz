package com.example.backendquiz.domain.question;
import com.example.backendquiz.domain.quiz.QuizAttemptService;
import com.example.backendquiz.domain.quiz.QuizCacheService;
import com.example.backendquiz.domain.quiz.dto.QuestionResponse;
import com.example.backendquiz.domain.quiz.dto.QuizSubmitRequest;
import com.example.backendquiz.domain.quiz.dto.QuizSubmitResponse;
import com.example.backendquiz.domain.user.User;
import com.example.backendquiz.domain.wrongnote.WrongNoteService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class QueryQuestionRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QQuestion question = QQuestion.question1;
    private final QuestionRepository questionRepository;
    private final WrongNoteService wrongNoteService;
    private final QuizCacheService quizCacheService;
    private final QuizAttemptService quizAttemptService;
    public QuestionResponse findRandomByCategoryId(Long categoryId, User user) {

        // 1. Redis에서 문제 목록 조회 (캐시 우선)
        List<QuestionResponse> allQuestions = quizCacheService.getQuestions(categoryId);

        if (allQuestions.isEmpty()) {
            throw new IllegalStateException("해당 카테고리에 문제가 없습니다.");
        }

        // 2. 로그인 유저면 푼 문제 제외
        List<QuestionResponse> candidates = allQuestions;
        if (user != null) {
            Set<Long> solvedIds = quizCacheService.getSolvedIds(user.getId(), categoryId);
            log.info("[QuestionCache] 푼 문제 수: {}", solvedIds.size());

            candidates = allQuestions.stream()
                    .filter(q -> !solvedIds.contains(q.getQuestionId()))
                    .toList();

            // 모든 문제 다 풀었으면 초기화 (처음부터 다시)
            if (candidates.isEmpty()) {
                log.info("[QuestionCache] 모든 문제 완료 - 초기화");
                candidates = allQuestions;
            }
        }

        // 3. 랜덤 1개 선택
        QuestionResponse randomQuestion = candidates.get(new Random().nextInt(candidates.size()));

        // 4. 푼 문제 목록에 추가
        if (user != null) {
            quizCacheService.addSolved(user.getId(), categoryId, randomQuestion.getQuestionId());
        }

        return new QuestionResponse(
                randomQuestion.getQuestionId(),
                randomQuestion.getQuestion(),
                randomQuestion.getOption1(),
                randomQuestion.getOption2(),
                randomQuestion.getOption3(),
                randomQuestion.getOption4()
        );
    }

    public QuizSubmitResponse checkedQuestionIsCorrect(QuizSubmitRequest quizSubmitRequest, User user) {

        Question getQuestion = questionRepository.findById(quizSubmitRequest.getQuestionId())
                .orElseThrow(() -> new RuntimeException("잘못된 문제"));

        boolean isCorrect = getQuestion.getAnswer() == quizSubmitRequest.getAnswer();
        log.info("정답 여부:{}", isCorrect);

        if (user != null && !isCorrect) {
            log.info("오답노트 addIncrease 접근:{},{}", user, getQuestion);
            wrongNoteService.addOrIncrease(user, getQuestion);
        }

        quizAttemptService.save(user, getQuestion, quizSubmitRequest.getAnswer(), isCorrect);

        return new QuizSubmitResponse(
                getQuestion.getId(),
                getQuestion.getCategory().getName(),
                getQuestion.getAnswer(),
                getQuestion.getExplanation(),
                isCorrect
        );
    }


}
