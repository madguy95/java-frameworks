package com.madadipouya.springkafkatest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPoint {

    private Long userId;

    private Double point;

    private LocalDate pointDate;
}
