package com.example.usermanagement.service;

import com.example.usermanagement.data.dto.MachineCreateDto;
import com.example.usermanagement.data.dto.MachineDto;
import com.example.usermanagement.data.dto.MachineScheduleDto;
import com.example.usermanagement.data.dto.MachineScheduleErrorDto;
import com.example.usermanagement.data.entities.enums.MachineStatus;

import java.util.List;

public interface MachineService {


    List<MachineDto> searchMachines(Long userId, String name, List<MachineStatus> statuses, Long from, Long to);
    MachineDto getMachineById(Long id);

    MachineDto createMachine(MachineCreateDto machineCreateDto);

    void attemptMachineStart(Long id);

    void attemptMachineStop(Long id);

    void attemptMachineDischarge(Long id);

    void attemptMachineDestroy(Long id);

    void machineStart(Long id) throws InterruptedException;

    void machineStop(Long id) throws InterruptedException;

    void machineDischarge(Long id) throws InterruptedException;

    void scheduleMachineStart(MachineScheduleDto machineScheduleDto);

    void scheduleMachineStop(MachineScheduleDto machineScheduleDto);

    void scheduleMachineDischarge(MachineScheduleDto machineScheduleDto);

    List<MachineScheduleErrorDto> getMachineScheduleActionErrors(Long userId);
}
