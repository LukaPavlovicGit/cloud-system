package com.example.cloudsystem.data.entities;

import com.example.cloudsystem.data.entities.enums.MachineAction;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class MachineSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long userId;

    @Enumerated(EnumType.STRING)
    private MachineAction targetAction;

    @Column
    private Long machineId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduleDate;

}
