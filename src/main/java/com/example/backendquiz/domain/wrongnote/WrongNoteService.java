package com.example.backendquiz.domain.wrongnote;

import com.example.backendquiz.domain.question.Question;
import com.example.backendquiz.domain.user.User;
import com.example.backendquiz.domain.wrongnote.dto.WrongNoteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WrongNoteService {

    private final WrongNoteRepository wrongNoteRepository;


    // 오답노트 추가 또는 카운트 증가
    @Transactional
    public void addOrIncrease(User user, Question question) {

        wrongNoteRepository.findByUserIdAndQuestionId(user.getId(), question.getId())
                .ifPresentOrElse(WrongNote::increaseCount,
                ()->new WrongNote(user, question)
                );

    }

    // 내 오답노트 목록 조회
    @Transactional(readOnly = true)
    public List<WrongNoteResponse> getMyWrongNotes(User user) {

        List<WrongNote> wrongNotes = wrongNoteRepository.findByUser(user);

        WrongNoteResponse response;

        List<WrongNoteResponse> list = new ArrayList<>();

        for (WrongNote wrongNote : wrongNotes) {

            response = new WrongNoteResponse(wrongNote);

            list.add(response);
        }

        return list;
    }

    // 오답노트 삭제
    @Transactional
    public void delete(Long wrongNoteId, User user) {
        WrongNote wrongNote = wrongNoteRepository.findById(wrongNoteId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 오답노트입니다."));

        // 본인 것만 삭제 가능
        if (!wrongNote.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }

        wrongNoteRepository.delete(wrongNote);
    }

}
