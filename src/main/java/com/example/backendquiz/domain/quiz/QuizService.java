package com.example.backendquiz.domain.quiz;
import com.example.backendquiz.auth.AuthUser;
import com.example.backendquiz.domain.question.QueryQuestionRepository;
import com.example.backendquiz.domain.quiz.dto.QuestionResponse;
import com.example.backendquiz.domain.quiz.dto.QuizSubmitRequest;
import com.example.backendquiz.domain.quiz.dto.QuizSubmitResponse;
import com.example.backendquiz.domain.user.User;
import com.example.backendquiz.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QueryQuestionRepository queryQuestionRepository;
    private final ActivityBroadCastService activityBroadcastService;
    private final UserRepository userRepository;

    @Transactional
    public QuestionResponse getRandomQuestion(Long categoryId, AuthUser authUser) {

        User user = validUser(authUser);

        return queryQuestionRepository.findRandomByCategoryId(categoryId, user);
    }

    @Transactional
    public QuizSubmitResponse submit(QuizSubmitRequest request, AuthUser authUser) {

        User user = validUser(authUser);

        QuizSubmitResponse response = queryQuestionRepository.checkedQuestionIsCorrect(request, user);
        //유저가 없으면 익명으로
        String nickname = user != null ? user.getNickname() : "익명";

        String category = response.getCategory().name();


        //여기서 방송
        activityBroadcastService.broadcast(nickname, category, response.isCorrect());

        return response;
    }

    @Transactional
    public User validUser(AuthUser user) {

        if (user == null) return null;

        return userRepository.findById(user.getUserId()).orElse(null);
    }
}
