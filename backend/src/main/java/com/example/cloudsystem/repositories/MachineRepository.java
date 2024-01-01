package com.example.cloudsystem.repositories;

import com.example.cloudsystem.data.entities.Machine;
import com.example.cloudsystem.data.entities.enums.MachineStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepository extends JpaRepositoryImplementation<Machine, Long> {

    Optional<Machine> findByUserId(Long userId);

    @Query("SELECT m FROM Machine m WHERE m.userId = :userId " +
            "AND (:name IS NULL OR m.name LIKE CONCAT('%', :name, '%')) " +
            "AND (coalesce(:statuses) IS NULL OR m.status IN (:statuses)) " +
            "AND (:from IS NULL OR m.createdDate >= :from) " +
            "AND (:to IS NULL OR m.createdDate <= :to)" +
            "AND m.active = true ")
    List<Machine> findAllByCriteria(@Param("userId") Long userId,
                                    @Param("name") String name,
                                    @Param("statuses") List<MachineStatus> statuses,
                                    @Param("from") Date from,
                                    @Param("to") Date to);
}

/*
, String name, List<MachineStatus> statuses, Date from, Date to
+

            "and (:statuses is null or m.status in (:statuses)) " +
            "and (:from is null or m.createdDate >= :from) " +
            "and (:to is null or m.createdDate <= :to) " +
            "and m.active = true "

 */