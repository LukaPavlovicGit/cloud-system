package com.example.usermanagement.data.entities;

import com.example.usermanagement.data.entities.enums.MachineAction;
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

    @Enumerated(EnumType.STRING)
    private MachineAction targetAction;

    @Column
    private Long machineId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date scheduleDate;

}
