package com.javbre.controller;

import com.javbre.dto.AssignRequest;
import com.javbre.dto.StudentExamView;
import com.javbre.persistence.entity.StudentExamEntity;
import com.javbre.service.AssignmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exams")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping("/{examId}/assign")
    public ResponseEntity<List<Long>> assignExam(@PathVariable Long examId, @RequestBody AssignRequest req) {
        List<StudentExamEntity> created = assignmentService.assignExam(examId, req);
        List<Long> ids = created.stream().map(StudentExamEntity::getId).collect(Collectors.toList());
        return ResponseEntity.ok(ids);
    }

    @GetMapping("/student-exams/{studentExamId}")
    public ResponseEntity<StudentExamView> getStudentExamView(@PathVariable Long studentExamId) {
        StudentExamView view = assignmentService.getStudentExamView(studentExamId);
        return ResponseEntity.ok(view);
    }
}

