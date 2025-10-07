package com.javbre.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "option_choice", indexes = @Index(columnList = "question_id"))
public class OptionChoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private QuestionEntity question;

    @Column(columnDefinition = "text")
    private String text;

    private boolean isCorrect;

}
