package com.javbre.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "student_answer", uniqueConstraints = @UniqueConstraint(columnNames = {"student_exam_id", "question_id"}))
public class StudentAnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private StudentExamEntity studentExam;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private QuestionEntity question;

    @ManyToOne(fetch = FetchType.LAZY)
    private OptionChoiceEntity chosenOption;

    private Instant answeredAt;

}

