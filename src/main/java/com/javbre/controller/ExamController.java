package com.javbre.controller;

import com.javbre.dto.ExamCreateRequest;
import com.javbre.persistence.entity.ExamEntity;
import com.javbre.service.ExamService;
import com.javbre.utilities.HeadersUtilities;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${controller.properties.base-path}/exams")
public class ExamController {

    private final ExamService examService;
    private final HeadersUtilities headersUtilities;

    public ExamController(ExamService examService, HeadersUtilities headersUtilities) {
        this.examService = examService;
        this.headersUtilities = headersUtilities;
    }

    @PostMapping
    public ResponseEntity<String> createExam(@RequestHeader HttpHeaders headers,
                                                 @RequestBody ExamCreateRequest req) {
        headersUtilities.validateHeaders(headers);
        String messageResponse = examService.createExam(req);
        return ResponseEntity.ok(messageResponse);
    }
}

