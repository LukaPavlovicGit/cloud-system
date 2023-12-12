package com.example.usermanagement.repositories;

import com.example.usermanagement.data.entities.Machine;
import com.example.usermanagement.data.entities.enums.MachineStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepositoryImplementation<Machine, Long> {

    Optional<Machine> findByUserId(Long userId);

    @Query("SELECT m FROM Machine m WHERE m.userId = :userId " +
            "AND (:name IS NULL OR m.name LIKE CONCAT('%', :name, '%')) " +
            "AND (:statuses IS NULL OR m.status IN :statuses) " +
            "AND (:from IS NULL OR m.createdDate >= :from) " +
            "AND (:to IS NULL OR m.createdDate <= :to)" +
            "AND m.active = true ")
    List<Machine> findAllByCriteria(Long userId, String name, List<MachineStatus> statuses, Date from, Date to);
}

/*
, String name, List<MachineStatus> statuses, Date from, Date to
+

            "and (:statuses is null or m.status in (:statuses)) " +
            "and (:from is null or m.createdDate >= :from) " +
            "and (:to is null or m.createdDate <= :to) " +
            "and m.active = true "

 */