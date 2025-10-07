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
public class QuestionDto {

    private String text;
    private java.math.BigDecimal score;
    private Integer position;
    private List<OptionDto> options;
}
