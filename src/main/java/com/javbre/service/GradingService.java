package com.javbre.service;

import com.javbre.dto.AnswerDto;
import com.javbre.dto.GradeResponse;
import com.javbre.dto.SubmitAnswersRequest;
import com.javbre.exception.BadRequestException;
import com.javbre.exception.NotFoundException;
import com.javbre.persistence.entity.*;
import com.javbre.persistence.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GradingService {
    private final StudentExamRepository studentExamRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final QuestionRepository questionRepository;
    private final OptionChoiceRepository optionChoiceRepository;
    private final ExamResultRepository examResultRepository;

    public GradingService(StudentExamRepository studentExamRepository,
                          StudentAnswerRepository studentAnswerRepository,
                          QuestionRepository questionRepository,
                          OptionChoiceRepository optionChoiceRepository,
                          ExamResultRepository examResultRepository) {
        this.studentExamRepository = studentExamRepository;
        this.studentAnswerRepository = studentAnswerRepository;
        this.questionRepository = questionRepository;
        this.optionChoiceRepository = optionChoiceRepository;
        this.examResultRepository = examResultRepository;
    }

    @Transactional
    public void submitAnswers(Long studentExamId, SubmitAnswersRequest req) {
        StudentExamEntity studentExam = studentExamRepository.findById(studentExamId).orElseThrow(() -> new NotFoundException("StudentExam not found"));
        // validar que preguntas correspondan al examen
        List<QuestionEntity> examQuestions = questionRepository.findAll()
                .stream()
                .filter(q -> q.getExam().getId().equals(studentExam.getExam().getId()))
                .toList();
        Map<Long, QuestionEntity> questionMap = examQuestions.stream().collect(Collectors.toMap(QuestionEntity::getId, q -> q));
        // guardar/actualizar respuestas
        for (AnswerDto answerDto : req.getAnswers()) {
            if (!questionMap.containsKey(answerDto.getQuestionId())) {
                throw new BadRequestException("La pregunta no pertenece al examen: " + answerDto.getQuestionId());
            }
            OptionChoiceEntity chosen = null;
            if (answerDto.getChosenOptionId() != null) {
                chosen = optionChoiceRepository.findById(answerDto.getChosenOptionId())
                        .orElseThrow(() -> new NotFoundException("Option not found: " + answerDto.getChosenOptionId()));
                // check que la opción pertenezca a la pregunta
                if (!chosen.getQuestion().getId().equals(answerDto.getQuestionId())) {
                    throw new BadRequestException("Option no corresponde a la pregunta");
                }
            }
            // buscar respuesta existente
            List<StudentAnswerEntity> existing = studentAnswerRepository.findByStudentExamId(studentExamId)
                    .stream()
                    .filter(sa -> sa.getQuestion().getId().equals(answerDto.getQuestionId()))
                    .toList();
            StudentAnswerEntity studentAnswer;
            if (!existing.isEmpty()) {
                studentAnswer = existing.getFirst();
                studentAnswer.setChosenOption(chosen);
                studentAnswer.setAnsweredAt(Instant.now());
            } else {
                studentAnswer = new StudentAnswerEntity();
                studentAnswer.setStudentExam(studentExam);
                studentAnswer.setQuestion(questionMap.get(answerDto.getQuestionId()));
                studentAnswer.setChosenOption(chosen);
                studentAnswer.setAnsweredAt(Instant.now());
            }
            studentAnswerRepository.save(studentAnswer);
        }
    }

    @Transactional
    public GradeResponse grade(Long studentExamId) {
        StudentExamEntity se = studentExamRepository.findById(studentExamId).orElseThrow(() -> new NotFoundException("StudentExam not found"));
        // cargar preguntas del examen
        List<QuestionEntity> questions = se.getExam().getQuestions();
        List<StudentAnswerEntity> answers = studentAnswerRepository.findByStudentExamId(studentExamId);

        Map<Long, StudentAnswerEntity> ansMap = answers.stream()
                .collect(Collectors.toMap(a -> a.getQuestion().getId(), a -> a));

        BigDecimal total = BigDecimal.ZERO;
        for (QuestionEntity question : questions) {
            StudentAnswerEntity studentAnswer = ansMap.get(question.getId());
            if (studentAnswer != null && studentAnswer.getChosenOption() != null && studentAnswer.getChosenOption().isCorrect()) {
                total = total.add(question.getScore());
            }
        }

        ExamResultEntity result = examResultRepository.findByStudentExamId(studentExamId).orElse(null);
        if (result == null) {
            result = new ExamResultEntity();
            result.setStudentExam(se);
        }
        result.setTotalScore(total);
        result.setGradedAt(Instant.now());
        examResultRepository.save(result);

        GradeResponse resp = new GradeResponse();
        resp.setTotalScore(total);
        resp.setGradedAt(result.getGradedAt().toString());
        return resp;
    }
}

