package com.example.usermanagement.data.entities;

import com.example.usermanagement.data.entities.enums.MachineStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Getter
@Setter
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MachineStatus status;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column
    private String name;

    @Column(nullable = false)
    private boolean busy;


    public Machine(){
        active = true;
        createdDate = Date.from(Instant.now());
        status = MachineStatus.STOPPED;
    }

}
