package com.example.cloudsystem.repositories;

import com.example.cloudsystem.data.entities.Permission;
import com.example.cloudsystem.data.entities.enums.PermissionType;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepositoryImplementation<Permission, Long> {

    List<Permission> findAllByPermissionIn(List<PermissionType> permissionTypes);
}
