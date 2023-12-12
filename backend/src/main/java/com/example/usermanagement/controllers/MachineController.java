package com.example.usermanagement.controllers;

import com.example.usermanagement.data.dto.MachineCreateDto;
import com.example.usermanagement.data.dto.MachineScheduleDto;
import com.example.usermanagement.data.entities.enums.MachineStatus;
import com.example.usermanagement.data.entities.enums.PermissionType;
import com.example.usermanagement.exceptions.CustomException;
import com.example.usermanagement.security.CheckSecurity;
import com.example.usermanagement.service.MachineService;
import com.example.usermanagement.service.impl.MachineServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/machines", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class MachineController {

    private static final Logger logger = LoggerFactory.getLogger(MachineController.class);

    @Qualifier(value = "machineServiceImpl")
    private final MachineService machineService;


    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }



    @CheckSecurity(permissions = {PermissionType.CAN_SEARCH_MACHINES})
    @GetMapping
    public ResponseEntity<?> machinesSearch(@RequestParam Long userId,
                                            @RequestParam(required = false) String name,
                                            @RequestParam(required = false) List<MachineStatus> statuses,
                                            @RequestParam(required = false) Long dateFrom,
                                            @RequestParam(required = false) Long dateTo
    ) {
        return ResponseEntity.ok(machineService.searchMachines(userId, name, statuses, dateFrom, dateTo));
    }

    @CheckSecurity(permissions = {PermissionType.CAN_CREATE_MACHINES})
    @PostMapping
    public ResponseEntity<?> machineCreate(@RequestBody MachineCreateDto machineCreateDto){
        return ResponseEntity.ok(machineService.createMachine(machineCreateDto));
    }

    @CheckSecurity(permissions = {PermissionType.CAN_START_MACHINES})
    @PutMapping("/machine-start/{id}")
    public ResponseEntity<?> attemptMachineStart(@PathVariable("id") Long id){
        logger.info("attempting machine start");
        try {
            machineService.attemptMachineStart(id);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (CustomException e) {
            logger.info("machine start unsuccessful");
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @CheckSecurity(permissions = {PermissionType.CAN_STOP_MACHINES})
    @PutMapping("/machine-stop/{id}")
    public ResponseEntity<?> attemptMachineStop(@PathVariable("id") Long id){
        logger.info("attempting machine stop");
        try {
            machineService.attemptMachineStop(id);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (CustomException e) {
            logger.info("machine stop unsuccessful");
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @CheckSecurity(permissions = {PermissionType.CAN_RESTART_MACHINES})
    @PutMapping("/machine-restart/{id}")
    public ResponseEntity<?> attemptMachineRestart(@PathVariable("id") Long id){
        logger.info("attempting machine restart");
        try {
            machineService.attemptMachineRestart(id);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (CustomException e) {
            logger.info("machine restart unsuccessful");
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @CheckSecurity(permissions = {PermissionType.CAN_DESTROY_MACHINES})
    @DeleteMapping("/machine-destroy/{id}")
    public ResponseEntity<?> attemptMachineDestroy(@PathVariable("id") Long id){
        logger.info("attempting machine destroy");
        try {
            machineService.attemptMachineDestroy(id);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (CustomException e) {
            logger.info("machine destroy unsuccessful");
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @CheckSecurity(permissions = {PermissionType.CAN_START_MACHINES})
    @PostMapping("/schedule-machine-start")
    public ResponseEntity<?> scheduleMachineStart(@RequestBody MachineScheduleDto machineScheduleDto){
        logger.info("scheduling machine start");
        try {
            machineService.scheduleStartMachine(machineScheduleDto);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (CustomException e) {
            logger.info("scheduling machine start unsuccessful");
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @CheckSecurity(permissions = {PermissionType.CAN_STOP_MACHINES})
    @PostMapping("/schedule-machine-stop")
    public ResponseEntity<?> scheduleMachineStop(@RequestBody MachineScheduleDto machineScheduleDto){
        logger.info("scheduling machine stop");
        try {
            machineService.scheduleStopMachine(machineScheduleDto);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (CustomException e) {
            logger.info("scheduling machine stop unsuccessful");
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @CheckSecurity(permissions = {PermissionType.CAN_RESTART_MACHINES})
    @PostMapping("/schedule-machine-restart")
    public ResponseEntity<?> scheduleMachineRestart(@RequestBody MachineScheduleDto machineScheduleDto){
        logger.info("scheduling machine restart");
        try {
            machineService.scheduleRestartMachine(machineScheduleDto);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (CustomException e) {
            logger.info("scheduling machine restart unsuccessful");
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

}
