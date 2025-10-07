package com.javbre.persistence.repository;

import com.javbre.persistence.entity.StudentExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentExamRepository extends JpaRepository<StudentExamEntity, Long> {

    Optional<StudentExamEntity> findByStudentIdAndExamId(Long studentId, Long examId);

}