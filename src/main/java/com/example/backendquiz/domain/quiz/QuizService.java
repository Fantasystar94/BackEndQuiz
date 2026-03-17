package com.example.backendquiz.domain.quiz;
import com.example.backendquiz.domain.category.CategoryRepository;
import com.example.backendquiz.domain.question.QueryQuestionRepository;
import com.example.backendquiz.domain.question.Question;
import com.example.backendquiz.domain.question.QuestionRepository;
import com.example.backendquiz.domain.quiz.dto.QuestionResponse;
import com.example.backendquiz.domain.quiz.dto.QuizSubmitRequest;
import com.example.backendquiz.domain.quiz.dto.QuizSubmitResponse;
import com.example.backendquiz.domain.user.User;
import com.example.backendquiz.domain.user.UserRepository;
import com.example.backendquiz.domain.wrongnote.WrongNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizService {

    private final QueryQuestionRepository queryQuestionRepository;


    @Transactional
    public QuestionResponse getRandomQuestion(Long categoryId) {

        return queryQuestionRepository.findRandomByCategoryId(categoryId);
    }

    @Transactional
    public QuizSubmitResponse submit(QuizSubmitRequest request, User user) {

        return queryQuestionRepository.checkedQuestionIsCorrect(request, user);
    }
}
