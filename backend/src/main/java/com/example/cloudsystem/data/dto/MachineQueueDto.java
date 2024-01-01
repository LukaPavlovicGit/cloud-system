package com.example.cloudsystem.data.dto;

import com.example.cloudsystem.data.entities.enums.MachineAction;
import lombok.Data;

import java.io.Serializable;

@Data
public class MachineQueueDto implements Serializable {

    private Long machineId;
    private MachineAction machineAction;

}
