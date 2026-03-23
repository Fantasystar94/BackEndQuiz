package com.example.backendquiz.domain.quiz;
import com.example.backendquiz.domain.question.QuestionRepository;
import com.example.backendquiz.domain.quiz.dto.QuestionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuizCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;

    // 카테고리별 문제 목록 캐시 TTL: 1시간
    private static final long QUESTION_CACHE_TTL = 60 * 60L;
    // 푼 문제 목록 TTL: 24시간 (토큰 만료시간과 동일하게)
    private static final long SOLVED_TTL = 60 * 60 * 24L;


    private String questionCacheKey(Long categoryId) {
        return "questions:category:" + categoryId;
    }

    private String solvedKey(Long userId, Long categoryId) {
        return "solved:" + userId + ":" + categoryId;
    }

    // 문제 목록 조회 (캐시 우선)
    public List<QuestionResponse> getQuestions(Long categoryId) {

        String key = questionCacheKey(categoryId);

        Object cached = redisTemplate.opsForValue().get(key);

        if (cached != null) {
            log.info("[QuestionCache] 캐시 히트 - categoryId: {}", categoryId);
            return objectMapper.convertValue(cached,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, QuestionResponse.class));
        }

        log.info("[QuestionCache] 캐시 미스 - DB 조회 categoryId: {}", categoryId);
        List<QuestionResponse> questions = questionRepository.findByCategoryId(categoryId)
                .stream()
                .map(q -> new QuestionResponse(
                        q.getId(),
                        q.getQuestion(),
                        q.getOption1(),
                        q.getOption2(),
                        q.getOption3(),
                        q.getOption4()
                ))
                .toList();

        redisTemplate.opsForValue().set(key, questions, QUESTION_CACHE_TTL, TimeUnit.SECONDS);
        return questions;
    }
    // 푼 문제 추가
    public void addSolved(Long userId, Long categoryId, Long questionId) {
        String key = solvedKey(userId, categoryId);
        redisTemplate.opsForSet().add(key, questionId.toString());
        redisTemplate.expire(key, SOLVED_TTL, TimeUnit.SECONDS);
        log.info("[QuestionCache] 푼 문제 추가 - userId:{} questionId:{}", userId, questionId);
    }

    // 푼 문제 목록 조회
    public Set<Long> getSolvedIds(Long userId, Long categoryId) {
        String key = solvedKey(userId, categoryId);
        Set<Object> solved = redisTemplate.opsForSet().members(key);
        if (solved == null) return Set.of();
        return solved.stream()
                .map(id -> Long.parseLong(id.toString()))
                .collect(Collectors.toSet());
    }

}
