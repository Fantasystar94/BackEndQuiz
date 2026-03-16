package com.example.backendquiz.domain.wrongnote;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WrongNoteRepository extends JpaRepository<WrongNote, Long> {

    Optional<WrongNote> findByUserIdAndQuestionId(Long userId, Long questionId);
}
