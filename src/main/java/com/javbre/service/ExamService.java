package com.javbre.service;

import com.javbre.dto.ExamCreateRequest;
import com.javbre.dto.OptionDto;
import com.javbre.dto.QuestionDto;
import com.javbre.exception.BadRequestException;
import com.javbre.persistence.entity.ExamEntity;
import com.javbre.persistence.entity.OptionChoiceEntity;
import com.javbre.persistence.entity.QuestionEntity;
import com.javbre.persistence.repository.ExamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.javbre.utilities.Utilities.localToInstant;

@Service
public class ExamService {
    private final ExamRepository examRepository;

    public ExamService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Transactional
    public String createExam(ExamCreateRequest req) {
        if (req.getPresentationLocal() == null || req.getPresentationTz() == null) {
            throw new BadRequestException("presentationLocal y presentationTz son requeridos");
        }

        Instant presentationInstant;
        try {
            presentationInstant = localToInstant(req.getPresentationLocal(), req.getPresentationTz());
        } catch (DateTimeException ex) {
            throw new BadRequestException("Formato de fecha/zona inválido: " + ex.getMessage());
        }

        ExamEntity exam = new ExamEntity();
        exam.setTitle(req.getTitle());
        exam.setDescription(req.getDescription());
        exam.setPresentationTime(presentationInstant);

        BigDecimal sum = BigDecimal.ZERO;

        if (req.getQuestions() == null || req.getQuestions().isEmpty()) {
            throw new BadRequestException("El examen debe contener al menos una pregunta");
        }

        List<QuestionEntity> questionEntityList = new ArrayList<>();
        for (QuestionDto qdto : req.getQuestions()) {
            // Validaciones por pregunta
            if (qdto.getOptions() == null || qdto.getOptions().size() != 4) {
                throw new BadRequestException("Cada pregunta debe tener exactamente 4 opciones");
            }
            long correctCount = qdto.getOptions().stream().filter(OptionDto::isCorrect).count();
            if (correctCount != 1) {
                throw new BadRequestException("Cada pregunta debe tener exactamente 1 opción correcta");
            }

            QuestionEntity question = new QuestionEntity();
            question.setText(qdto.getText());
            question.setScore(qdto.getScore());
            question.setPosition(qdto.getPosition());
            question.setExam(exam);

            List<OptionChoiceEntity> optionChoiceEntityList = new ArrayList<>();
            for (OptionDto odto : qdto.getOptions()) {
                OptionChoiceEntity optionChoice = new OptionChoiceEntity();
                optionChoice.setText(odto.getText());
                optionChoice.setCorrect(odto.isCorrect());
                optionChoice.setQuestion(question);
                optionChoiceEntityList.add(optionChoice);
            }
            question.setOptions(optionChoiceEntityList);
            questionEntityList.add(question);

            if (qdto.getScore() != null) {
                sum = sum.add(qdto.getScore());
            } else {
                throw new BadRequestException("Cada pregunta debe tener puntaje definido");
            }
        }

        // validar suma = 100
        if (sum.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new BadRequestException("La suma de los puntajes debe ser 100. Actualmente es de: " + sum);
        }

        // fijar preguntas en el examen (no olvides el cascade en la entidad)
        exam.setQuestions(questionEntityList);

        examRepository.save(exam);
        return "Examen creado con éxito";
    }

}

