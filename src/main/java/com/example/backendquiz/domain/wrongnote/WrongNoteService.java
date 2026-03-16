package com.example.backendquiz.domain.wrongnote;

import com.example.backendquiz.domain.question.Question;
import com.example.backendquiz.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WrongNoteService {

    private final WrongNoteRepository wrongNoteRepository;

    @Transactional
    public void saveWrongNote(User user, Question question) {

        Optional<WrongNote> optional =
                wrongNoteRepository.findByUserIdAndQuestionId(user.getId(), question.getId());

        if (optional.isPresent()) {
            optional.get().increaseCount();
        } else {
            WrongNote wrongNote = new WrongNote(user, question);
            wrongNoteRepository.save(wrongNote);
        }
    }

}
