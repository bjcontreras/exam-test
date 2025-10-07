package com.javbre.service;

import com.javbre.dto.AssignRequest;
import com.javbre.dto.StudentExamView;
import com.javbre.exception.NotFoundException;
import com.javbre.persistence.entity.ExamEntity;
import com.javbre.persistence.entity.StudentEntity;
import com.javbre.persistence.entity.StudentExamEntity;
import com.javbre.persistence.repository.ExamRepository;
import com.javbre.persistence.repository.StudentExamRepository;
import com.javbre.persistence.repository.StudentRepository;
import com.javbre.utilities.Utilities;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssignmentService {
    private final StudentRepository studentRepository;
    private final ExamRepository examRepository;
    private final StudentExamRepository studentExamRepository;

    public AssignmentService(StudentRepository studentRepository, ExamRepository examRepository, StudentExamRepository studentExamRepository) {
        this.studentRepository = studentRepository;
        this.examRepository = examRepository;
        this.studentExamRepository = studentExamRepository;
    }

    @Transactional
    public List<StudentExamEntity> assignExam(Long examId, AssignRequest req) {
        ExamEntity exam = examRepository.findById(examId).orElseThrow(() -> new NotFoundException("Exam not found"));
        Instant now = Instant.now();
        List<StudentExamEntity> created = new ArrayList<>();
        for (Long studentId : req.getStudentIds()) {
            StudentEntity student = studentRepository.findById(studentId).orElseThrow(() -> new NotFoundException("Student not found: " + studentId));
            // evitar duplicados
            if (studentExamRepository.findByStudentIdAndExamId(student.getId(), exam.getId()).isPresent()) continue;

            StudentExamEntity studentExam = new StudentExamEntity();
            studentExam.setStudent(student);
            studentExam.setExam(exam);
            studentExam.setAssignedAt(now);
            created.add(studentExamRepository.save(studentExam));
        }
        return created;
    }

    @Transactional(readOnly = true)
    public StudentExamView getStudentExamView(Long studentExamId) {
        StudentExamEntity studentExam = studentExamRepository.findById(studentExamId).orElseThrow(() -> new NotFoundException("StudentExam not found"));
        StudentEntity student = studentExam.getStudent();
        ExamEntity exam = studentExam.getExam();
        StudentExamView view = new StudentExamView();
        view.setStudentExamId(studentExam.getId());
        view.setExamId(exam.getId());
        view.setExamTitle(exam.getTitle());
        view.setPresentationTz(student.getTimezone());
        view.setPresentationLocal(Utilities.instantToLocalString(exam.getPresentationTime(), student.getTimezone()));
        return view;
    }
}

