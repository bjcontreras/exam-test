package com.javbre.controller;

import com.javbre.dto.ExamCreateRequest;
import com.javbre.persistence.entity.ExamEntity;
import com.javbre.service.ExamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public ResponseEntity<ExamEntity> createExam(@RequestBody ExamCreateRequest req) {
        ExamEntity exam = examService.createExam(req);
        return ResponseEntity.ok(exam);
    }
}

