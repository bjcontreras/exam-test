package com.javbre.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "student_exam", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "exam_id"}))
public class StudentExamEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private StudentEntity student;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ExamEntity exam;

    private Instant assignedAt;

}
