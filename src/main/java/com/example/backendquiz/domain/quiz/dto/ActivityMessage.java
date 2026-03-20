package com.example.backendquiz.domain.quiz.dto;

import lombok.Getter;

@Getter
public class ActivityMessage {
    private final String message;
    private final String nickname;
    private final String category;
    private final boolean correct;
    private final long timestamp;

    public ActivityMessage(String nickname, String category, boolean correct) {
        this.nickname = nickname;
        this.category = category;
        this.correct = correct;
        this.timestamp = System.currentTimeMillis();
        this.message = correct
                ? "🎉 " + nickname + "님이 " + category + " 문제를 맞혔습니다!"
                : "😅 " + nickname + "님이 " + category + " 문제를 틀렸습니다!";
    }
}
