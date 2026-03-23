package com.example.backendquiz.domain.quiz;

import com.example.backendquiz.domain.quiz.dto.ActivityMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityBroadCastService {

    private final SimpMessagingTemplate messagingTemplate;

    public void broadcast(String nickname, String category, boolean correct) {

        ActivityMessage message = new ActivityMessage(nickname, category, correct);

        log.info("[소켓 메세지] : {}",message.getMessage());

        messagingTemplate.convertAndSend("/topic/activity", message);


    }
}
