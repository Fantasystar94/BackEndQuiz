package com.example.backendquiz.domain.quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {

    // 정답 수 기준 TOP 10 유저
    @Query("""
        SELECT a.user.nickname,
               SUM(CASE WHEN a.correct = true THEN 1 ELSE 0 END) as correctCount,
               COUNT(a) as totalCount
        FROM QuizAttempt a
        GROUP BY a.user.id, a.user.nickname
        ORDER BY correctCount DESC
        LIMIT 10
    """)
    List<Object[]> findTop10ByCorrectCount();
}
