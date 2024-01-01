package com.example.cloudsystem.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineCreateDto {

    private Long userId;
    private String name;

}
