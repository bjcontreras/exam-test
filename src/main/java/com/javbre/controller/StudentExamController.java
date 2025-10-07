package com.javbre.controller;

import com.javbre.dto.GradeResponse;
import com.javbre.dto.SubmitAnswersRequest;
import com.javbre.service.GradingService;
import com.javbre.utilities.HeadersUtilities;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${controller.properties.base-path}/student-exams")
public class StudentExamController {

    private final GradingService gradingService;
    private final HeadersUtilities headersUtilities;

    public StudentExamController(GradingService gradingService, HeadersUtilities headersUtilities) {
        this.gradingService = gradingService;
        this.headersUtilities = headersUtilities;
    }

    @PostMapping("/{studentExamId}/answers")
    public ResponseEntity<Void> submitAnswers(@RequestHeader HttpHeaders headers,
                                              @PathVariable Long studentExamId, @RequestBody SubmitAnswersRequest req) {
        headersUtilities.validateHeaders(headers);
        gradingService.submitAnswers(studentExamId, req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{studentExamId}/grade")
    public ResponseEntity<GradeResponse> grade(@RequestHeader HttpHeaders headers,
                                               @PathVariable Long studentExamId) {
        headersUtilities.validateHeaders(headers);
        GradeResponse gradeResponse = gradingService.grade(studentExamId);
        return ResponseEntity.ok(gradeResponse);
    }
}

