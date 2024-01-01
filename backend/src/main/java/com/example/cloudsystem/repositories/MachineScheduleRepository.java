package com.example.cloudsystem.repositories;

import com.example.cloudsystem.data.entities.MachineSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MachineScheduleRepository extends JpaRepository<MachineSchedule, Long> {
    List<MachineSchedule> findAllByScheduleDateBefore(Date date);

}
