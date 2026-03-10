package com.example.backendquiz.domain.quizattempt;

import com.example.backendquiz.common.BaseEntity;
import com.example.backendquiz.domain.question.Question;
import com.example.backendquiz.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="quiz_attempts")
@NoArgsConstructor
@Getter
public class QuizAttempt extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    private int selectedAnswer;

    private boolean correct;

    public QuizAttempt(User user, Question question, int selectedAnswer, boolean correct) {
        this.user = user;
        this.question = question;
        this.selectedAnswer = selectedAnswer;
        this.correct = correct;
    }
}
