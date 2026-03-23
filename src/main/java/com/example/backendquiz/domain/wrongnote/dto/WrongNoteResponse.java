package com.example.backendquiz.domain.wrongnote.dto;

import com.example.backendquiz.domain.wrongnote.WrongNote;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WrongNoteResponse {
    private Long id;
    private Long questionId;
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int answer;
    private String explanation;
    private int wrongCount;

    public WrongNoteResponse(WrongNote wrongNote) {
        this.id = wrongNote.getId();
        this.questionId = wrongNote.getQuestion().getId();
        this.question = wrongNote.getQuestion().getQuestion();
        this.option1 = wrongNote.getQuestion().getOption1();
        this.option2 = wrongNote.getQuestion().getOption2();
        this.option3 = wrongNote.getQuestion().getOption3();
        this.option4 = wrongNote.getQuestion().getOption4();
        this.answer = wrongNote.getQuestion().getAnswer();
        this.explanation = wrongNote.getQuestion().getExplanation();
        this.wrongCount = wrongNote.getWrongCount();
    }

}

