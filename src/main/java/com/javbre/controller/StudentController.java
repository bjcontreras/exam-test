package com.javbre.controller;

import com.javbre.dto.StudentCreateRequest;
import com.javbre.persistence.entity.StudentEntity;
import com.javbre.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentEntity> createStudent(@RequestBody StudentCreateRequest req) {
        StudentEntity student = studentService.createStudent(req);
        return ResponseEntity.ok(student);
    }
}

