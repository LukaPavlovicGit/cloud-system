package com.example.usermanagement.repositories;

import com.example.usermanagement.data.entities.MachineScheduleError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MachineScheduleErrorRepository extends JpaRepository<MachineScheduleError, Long> {

}
