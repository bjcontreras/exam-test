package com.javbre.persistence.repository;

import com.javbre.persistence.entity.StudentAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswerEntity, Long> {

    List<StudentAnswerEntity> findByStudentExamId(Long studentExamId);
}

