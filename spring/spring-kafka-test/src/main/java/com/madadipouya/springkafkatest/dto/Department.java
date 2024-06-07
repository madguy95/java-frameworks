package com.madadipouya.springkafkatest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {

    private String uuid;

    private String departmentName;

    private String departmentCode;

}
