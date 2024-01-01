package com.example.cloudsystem.data.dto;

import com.example.cloudsystem.data.entities.enums.MachineAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MachineScheduleErrorDto {

    private Long userId;

    private String message;

    private MachineAction action;

    private Long machineId;

    private Date dateError;
}
