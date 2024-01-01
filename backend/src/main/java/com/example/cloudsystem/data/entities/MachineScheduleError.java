package com.example.cloudsystem.data.entities;

import com.example.cloudsystem.data.entities.enums.MachineAction;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class MachineScheduleError {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Column
    private String message;

    @Enumerated(EnumType.STRING)
    private MachineAction action;

    @Column
    private Long machineId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateError;
}
