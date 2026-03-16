package com.example.backendquiz.domain.question;

import com.example.backendquiz.common.BaseEntity;
import com.example.backendquiz.domain.category.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="questions")
@NoArgsConstructor
@Getter
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String option1;

    @Column(nullable = false)
    private String option2;

    @Column(nullable = false)
    private String option3;

    @Column(nullable = false)
    private String option4;

    @Column(nullable = false)
    private int answer;

    private String explanation;

    public Question(
            Category category,
            String question,
            String option1,
            String option2,
            String option3,
            String option4,
            int answer,
            String explanation
    ) {
        this.category = category;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.explanation = explanation;
    }
}
