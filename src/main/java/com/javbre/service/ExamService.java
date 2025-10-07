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
    public ExamEntity createExam(ExamCreateRequest req) {
        if (req.getPresentationLocal() == null || req.getPresentationTz() == null) {
            throw new BadRequestException("presentationLocal y presentationTz son requeridos");
        }
        Instant presentationInstant = localToInstant(req.getPresentationLocal(), req.getPresentationTz());

        ExamEntity exam = new ExamEntity();
        exam.setTitle(req.getTitle());
        exam.setDescription(req.getDescription());
        exam.setPresentationTime(presentationInstant);

        // agregar preguntas y opciones
        BigDecimal sum = BigDecimal.ZERO;
        if (req.getQuestions() != null) {
            List<QuestionEntity> questionEntityList = new ArrayList<>();
            for (QuestionDto qdto : req.getQuestions()) {
                QuestionEntity question = new QuestionEntity();
                question.setText(qdto.getText());
                question.setScore(qdto.getScore());
                question.setPosition(qdto.getPosition());
                if (qdto.getOptions() != null) {
                    List<OptionChoiceEntity> optionChoiceEntityList = new ArrayList<>();
                    for (OptionDto odto : qdto.getOptions()) {
                        OptionChoiceEntity optionChoice = new OptionChoiceEntity();
                        optionChoice.setText(odto.getText());
                        optionChoice.setCorrect(odto.isCorrect());
                        optionChoiceEntityList.add(optionChoice);
                    }
                    question.setOptions(optionChoiceEntityList);
                }
                questionEntityList.add(question);
                if (qdto.getScore() != null) sum = sum.add(qdto.getScore());
            }
            exam.setQuestions(questionEntityList);
        }

        // validar suma = 100
        if (sum.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new BadRequestException("La suma de puntajes debe ser 100. Actual: " + sum);
        }

        return examRepository.save(exam);
    }
}

