package com.javbre.controller;

import com.javbre.dto.StudentCreateRequest;
import com.javbre.persistence.entity.StudentEntity;
import com.javbre.service.StudentService;
import com.javbre.utilities.HeadersUtilities;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${controller.properties.base-path}/students")
public class StudentController {

    private final StudentService studentService;
    private final HeadersUtilities headersUtilities;

    public StudentController(StudentService studentService, HeadersUtilities headersUtilities) {
        this.studentService = studentService;
        this.headersUtilities = headersUtilities;
    }

    @PostMapping
    public ResponseEntity<String> createStudent(@RequestHeader HttpHeaders headers,
                                                       @RequestBody StudentCreateRequest req) {
        headersUtilities.validateHeaders(headers);
        String messageResponse = studentService.createStudent(req);
        return ResponseEntity.ok(messageResponse);
    }
}

