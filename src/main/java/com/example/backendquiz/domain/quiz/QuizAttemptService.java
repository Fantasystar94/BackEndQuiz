package com.example.backendquiz.domain.quiz;

import com.example.backendquiz.domain.question.Question;
import com.example.backendquiz.domain.quiz.dto.RankingResponse;
import com.example.backendquiz.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;

    // QuizAttempt 저장
    @Transactional
    public void save(User user, Question question, int selectedAnswer, boolean correct) {
        if (user == null) return;
        quizAttemptRepository.save(new QuizAttempt(user, question, selectedAnswer, correct));
    }

    // 랭킹 TOP 10 조회
    @Transactional(readOnly = true)
    public List<RankingResponse> getTop10Ranking() {

        List<Object[]> results = quizAttemptRepository.findTop10ByCorrectCount();

        List<RankingResponse> ranking = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {

            Object[] row = results.get(i);

            String nickname = (String) row[0];

            long correctCount = ((Number) row[1]).longValue();

            long totalCount = ((Number) row[2]).longValue();

            double correctRate = totalCount > 0
                    ? Math.round((double) correctCount / totalCount * 1000) / 10.0
                    : 0.0;

            ranking.add(new RankingResponse(i + 1, nickname, correctCount, totalCount, correctRate));

        }

        return ranking;
    }
}
