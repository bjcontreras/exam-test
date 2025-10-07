package com.javbre.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentExamView {

    private Long studentExamId;
    private Long examId;
    private String examTitle;
    private String presentationLocal; // e.g. "2025-10-10T09:00:00"
    private String presentationTz;
}
