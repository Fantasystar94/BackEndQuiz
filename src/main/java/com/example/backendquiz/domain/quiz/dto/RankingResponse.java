package com.example.backendquiz.domain.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class RankingResponse {
    private final int rank;
    private final String nickname;
    private final long correctCount;
    private final long totalCount;
    private final double correctRate;
}
