package com.example.cloudsystem.data.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MachineScheduleDto implements Serializable {

    private Long userId;
    private Long machineId;
    private Long scheduleDate;

}