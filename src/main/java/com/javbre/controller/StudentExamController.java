package com.javbre.controller;

import com.javbre.dto.GradeResponse;
import com.javbre.dto.SubmitAnswersRequest;
import com.javbre.service.GradingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student-exams")
public class StudentExamController {

    private final GradingService gradingService;

    public StudentExamController(GradingService gradingService) {
        this.gradingService = gradingService;
    }

    @PostMapping("/{studentExamId}/answers")
    public ResponseEntity<Void> submitAnswers(@PathVariable Long studentExamId, @RequestBody SubmitAnswersRequest req) {
        gradingService.submitAnswers(studentExamId, req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{studentExamId}/grade")
    public ResponseEntity<GradeResponse> grade(@PathVariable Long studentExamId) {
        GradeResponse gradeResponse = gradingService.grade(studentExamId);
        return ResponseEntity.ok(gradeResponse);
    }
}

