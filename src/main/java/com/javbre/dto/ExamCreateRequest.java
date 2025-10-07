package com.javbre.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamCreateRequest {

    private String title;
    private String description;
    private String presentationLocal; //2025-10-10T09:00:00
    private String presentationTz;
    private List<QuestionDto> questions;
}
