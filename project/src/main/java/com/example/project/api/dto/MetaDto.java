package com.example.project.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MetaDto {

    @JsonProperty("total_count") // json 응답값의 키값과 매핑 시켜주는 역할
    private Integer totalCount;

}
