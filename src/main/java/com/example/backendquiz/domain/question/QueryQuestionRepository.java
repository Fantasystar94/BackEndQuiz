package com.example.backendquiz.domain.question;

import com.example.backendquiz.domain.quiz.dto.QuestionResponse;
import com.example.backendquiz.domain.quiz.dto.QuizSubmitRequest;
import com.example.backendquiz.domain.quiz.dto.QuizSubmitResponse;
import com.example.backendquiz.domain.user.User;
import com.example.backendquiz.domain.wrongnote.WrongNote;
import com.example.backendquiz.domain.wrongnote.WrongNoteRepository;
import com.example.backendquiz.domain.wrongnote.WrongNoteService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Random;
@Repository
@RequiredArgsConstructor
public class QueryQuestionRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final QQuestion question = QQuestion.question1;
    private final QuestionRepository questionRepository;
    private final WrongNoteService wrongNoteService;

    public QuestionResponse findRandomByCategoryId(Long categoryId) {


        // 전체 count 조회
        Long count = jpaQueryFactory
                .select(question.count())
                .from(question)
                .where(question.category.id.eq(categoryId))
                .fetchOne();

        if (count == null || count == 0) {
            throw new IllegalStateException("해당 카테고리에 문제가 없습니다.");
        }

        // 랜덤 offset 계산
        int offset = new Random().nextInt(count.intValue());

        return jpaQueryFactory
                .select(Projections.constructor(QuestionResponse.class,
                        question.id,
                        question.question,
                        question.option1,
                        question.option2,
                        question.option3,
                        question.option4
                ))
                .from(question)
                .where(question.category.id.eq(categoryId))
                .offset(offset)
                .limit(1)
                .fetchOne();
    }

    public QuizSubmitResponse checkedQuestionIsCorrect(QuizSubmitRequest quizSubmitRequest, User user) {

        //쿼리 조회 후 참거짓만 추가해 반환
        Question getQuestion = questionRepository.findById(quizSubmitRequest.getQuestionId()).orElseThrow(() -> new RuntimeException("잘못된 문제"));

        boolean isCorrect = getQuestion.getAnswer() == quizSubmitRequest.getAnswer();

        //오답노트 서비스 가져옴
        if (!isCorrect) {
            wrongNoteService.addOrIncrease(user, getQuestion);
        }

        return new QuizSubmitResponse(getQuestion.getId(), getQuestion.getAnswer(), getQuestion.getExplanation(), isCorrect);

    }


}
