package com.example.usermanagement.data.dto;

import com.example.usermanagement.data.entities.enums.MachineStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MachineDto {

    private Long id;
    private Long userId;
    private MachineStatus status;
    private Date createdAt;
    private boolean active;
    private String name;

    public MachineDto(){
        id = (long) -1;
        userId = (long) -1;
        status = MachineStatus.STOPPED;
        active = true;
    }
}
