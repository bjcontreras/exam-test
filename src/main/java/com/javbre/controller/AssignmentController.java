package com.javbre.controller;

import com.javbre.dto.AssignRequest;
import com.javbre.dto.StudentExamView;
import com.javbre.persistence.entity.StudentExamEntity;
import com.javbre.service.AssignmentService;
import com.javbre.utilities.HeadersUtilities;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${controller.properties.base-path}/exams")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final HeadersUtilities headersUtilities;

    public AssignmentController(AssignmentService assignmentService, HeadersUtilities headersUtilities) {
        this.assignmentService = assignmentService;
        this.headersUtilities = headersUtilities;
    }

    @PostMapping("/{examId}/assign")
    public ResponseEntity<List<Long>> assignExam(@RequestHeader HttpHeaders headers,
                                                 @PathVariable Long examId,
                                                 @RequestBody AssignRequest req) {
        headersUtilities.validateHeaders(headers);
        List<StudentExamEntity> created = assignmentService.assignExam(examId, req);
        List<Long> ids = created.stream().map(StudentExamEntity::getId).collect(Collectors.toList());
        return ResponseEntity.ok(ids);
    }

    @GetMapping("/student-exams/{studentExamId}")
    public ResponseEntity<StudentExamView> getStudentExamView(@RequestHeader HttpHeaders headers,
                                                              @PathVariable Long studentExamId) {
        headersUtilities.validateHeaders(headers);
        StudentExamView view = assignmentService.getStudentExamView(studentExamId);
        return ResponseEntity.ok(view);
    }
}

