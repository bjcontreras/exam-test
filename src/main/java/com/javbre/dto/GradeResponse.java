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
public class GradeResponse {

    @JsonProperty("totalScore")
    private BigDecimal totalScore;
    @JsonProperty("gradedAt")
    private String gradedAt;
}
