package com.javbre.service;

import com.javbre.dto.StudentCreateRequest;
import com.javbre.persistence.entity.StudentEntity;
import com.javbre.persistence.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Transactional
    public String createStudent(StudentCreateRequest req) {
        StudentEntity student = new StudentEntity();
        student.setName(req.getName());
        student.setAge(req.getAge());
        student.setCity(req.getCity());
        student.setTimezone(req.getTimezone());
        studentRepository.save(student);
        return "Estudiante creado con éxito";
    }
}

