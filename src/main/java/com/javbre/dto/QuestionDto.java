package com.javbre.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {

    @JsonProperty("text")
    private String text;
    @JsonProperty("score")
    private BigDecimal score;
    @JsonProperty("position")
    private Integer position;
    @JsonProperty("options")
    private List<OptionDto> options;
}
