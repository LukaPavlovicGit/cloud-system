package com.example.usermanagement.bootstrap;

import com.example.usermanagement.data.entities.Permission;
import com.example.usermanagement.data.entities.User;
import com.example.usermanagement.data.entities.enums.PermissionType;
import com.example.usermanagement.repositories.PermissionRepository;
import com.example.usermanagement.repositories.UserRepository;
import com.example.usermanagement.service.impl.MachineServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MachineServiceImpl.class);

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    public BootstrapData(UserRepository userRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        logger.info("DATA LOADING IN PROGRESS...");

        Permission permissionUserCreate = new Permission();
        permissionUserCreate.setPermission(PermissionType.CAN_CREATE_USERS);

        Permission permissionUserRead = new Permission();
        permissionUserRead.setPermission(PermissionType.CAN_READ_USERS);

        Permission permissionUserUpdate = new Permission();
        permissionUserUpdate.setPermission(PermissionType.CAN_UPDATE_USERS);

        Permission permissionUserDelete = new Permission();
        permissionUserDelete.setPermission(PermissionType.CAN_DELETE_USERS);

        Permission permissionMachineCreate = new Permission();
        permissionMachineCreate.setPermission(PermissionType.CAN_CREATE_MACHINES);

        Permission permissionMachineSearch = new Permission();
        permissionMachineSearch.setPermission(PermissionType.CAN_SEARCH_MACHINES);

        Permission permissionMachineStart = new Permission();
        permissionMachineStart.setPermission(PermissionType.CAN_START_MACHINES);

        Permission permissionMachineStop = new Permission();
        permissionMachineStop.setPermission(PermissionType.CAN_STOP_MACHINES);

        Permission permissionMachineRestart = new Permission();
        permissionMachineRestart.setPermission(PermissionType.CAN_RESTART_MACHINES);

        Permission permissionMachineDestroy = new Permission();
        permissionMachineDestroy.setPermission(PermissionType.CAN_DESTROY_MACHINES);


        //posto je u useru @Cascade(CascadeType.PERSIST) -> ne sme da se radi!
//        roleRepository.saveAll(List.of(roleCreate, roleRead, roleUpdate, roleDelete));


        User admin = new User();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setEmail("admin@admin.com");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setPermissions(List.of(
                permissionUserCreate,
                permissionUserRead,
                permissionUserUpdate,
                permissionUserDelete,
                permissionMachineCreate,
                permissionMachineSearch,
                permissionMachineStart,
                permissionMachineStop,
                permissionMachineRestart,
                permissionMachineDestroy
        ));
        userRepository.save(admin);

        logger.info("DATA LOADING FINISHED...");
    }

}
