package com.javbre.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("presentationLocal")
    private String presentationLocal; //2025-10-10T09:00:00
    @JsonProperty("presentationTz")
    private String presentationTz;
    @JsonProperty("questions")
    private List<QuestionDto> questions;
}
