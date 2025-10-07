package com.javbre.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentExamView {

    @JsonProperty("studentExamId")
    private Long studentExamId;
    @JsonProperty("examId")
    private Long examId;
    @JsonProperty("examTitle")
    private String examTitle;
    @JsonProperty("presentationLocal")
    private String presentationLocal; // e.g. "2025-10-10T09:00:00"
    @JsonProperty("presentationTz")
    private String presentationTz;
}
