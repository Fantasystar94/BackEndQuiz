package com.example.backendquiz.domain.wrongnote;

import com.example.backendquiz.common.BaseEntity;
import com.example.backendquiz.domain.question.Question;
import com.example.backendquiz.domain.user.User;
import jakarta.persistence.*;

@Entity
@Table(name="wrong_notes")
public class WrongNote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    private int wrongCount;

    public WrongNote(User user, Question question) {
        this.user = user;
        this.question = question;
        this.wrongCount = 1;
    }

    public void increaseCount() {
        this.wrongCount++;
    }
}
