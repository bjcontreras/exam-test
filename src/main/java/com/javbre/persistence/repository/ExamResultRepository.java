package com.javbre.persistence.repository;

import com.javbre.persistence.entity.ExamResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamResultRepository extends JpaRepository<ExamResultEntity, Long> {

    java.util.Optional<ExamResultEntity> findByStudentExamId(Long studentExamId);
}

