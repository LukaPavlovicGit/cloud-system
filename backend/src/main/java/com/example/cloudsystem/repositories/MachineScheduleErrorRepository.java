package com.example.cloudsystem.repositories;

import com.example.cloudsystem.data.entities.MachineScheduleError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineScheduleErrorRepository extends JpaRepository<MachineScheduleError, Long> {
    @Query("SELECT m FROM MachineScheduleError m WHERE m.userId = :userId")
    List<MachineScheduleError> findAllByUserId(Long userId);
}
