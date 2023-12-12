package com.example.usermanagement.service;

import com.example.usermanagement.data.dto.MachineCreateDto;
import com.example.usermanagement.data.dto.MachineDto;
import com.example.usermanagement.data.dto.MachineScheduleDto;
import com.example.usermanagement.data.entities.enums.MachineStatus;

import java.util.List;

public interface MachineService {


    List<MachineDto> searchMachines(Long userId, String name, List<MachineStatus> statuses, Long from, Long to);

    MachineDto createMachine(MachineCreateDto machineCreateDto);

    void attemptMachineStart(Long id);

    void attemptMachineStop(Long id);

    void attemptMachineRestart(Long id);

    void attemptMachineDestroy(Long id);

    void machineStart(Long id) throws InterruptedException;

    void machineStop(Long id) throws InterruptedException;

    void machineRestart(Long id) throws InterruptedException;

    void scheduleStartMachine(MachineScheduleDto machineScheduleDto);

    void scheduleStopMachine(MachineScheduleDto machineScheduleDto);

    void scheduleRestartMachine(MachineScheduleDto machineScheduleDto);
}
