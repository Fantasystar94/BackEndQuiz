package com.example.backendquiz.domain.quizattempt;

import com.example.backendquiz.domain.question.Question;
import com.example.backendquiz.domain.question.QuestionRepository;
import com.example.backendquiz.domain.user.User;
import com.example.backendquiz.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizAttemptService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuizAttemptRepository quizAttemptRepository;

    @Transactional
    public boolean solveQuestion(Long userId, Long questionId, int selectedAnswer) {

        User user = userRepository.findById(userId)
                .orElseThrow();

        Question question = questionRepository.findById(questionId)
                .orElseThrow();

        boolean correct = question.getAnswer() == selectedAnswer;

        QuizAttempt attempt =
                new QuizAttempt(user, question, selectedAnswer, correct);

        quizAttemptRepository.save(attempt);

        return correct;
    }
}
