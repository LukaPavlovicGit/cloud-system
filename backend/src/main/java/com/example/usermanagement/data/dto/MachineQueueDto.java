package com.example.usermanagement.data.dto;

import com.example.usermanagement.data.entities.enums.MachineAction;
import lombok.Data;

import java.io.Serializable;

@Data
public class MachineQueueDto implements Serializable {

    private Long machineId;
    private MachineAction machineAction;

}
