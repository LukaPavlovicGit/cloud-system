package com.example.usermanagement.data.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MachineScheduleDto implements Serializable {

    private Long machineId;
    private Long scheduleDate;

}