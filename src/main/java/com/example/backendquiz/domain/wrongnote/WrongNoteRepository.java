package com.example.backendquiz.domain.wrongnote;

import com.example.backendquiz.domain.question.Question;
import com.example.backendquiz.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface WrongNoteRepository extends JpaRepository<WrongNote, Long> {

    Optional<WrongNote> findByUserIdAndQuestionId(Long userId, Long questionId);

    Optional<WrongNote> findByUserAndQuestion(User user, Question question);

    List<WrongNote> findByUser(User user);
}
