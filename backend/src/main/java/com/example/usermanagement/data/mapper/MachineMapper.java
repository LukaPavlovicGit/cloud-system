package com.example.usermanagement.data.mapper;

import com.example.usermanagement.data.dto.MachineCreateDto;
import com.example.usermanagement.data.dto.MachineDto;
import com.example.usermanagement.data.dto.MachineScheduleErrorDto;
import com.example.usermanagement.data.entities.Machine;
import com.example.usermanagement.data.entities.MachineScheduleError;
import org.springframework.stereotype.Component;


@Component
public class MachineMapper {

    public Machine MachineCreateDtoToMachine(MachineCreateDto machineCreateDto){

        Machine machine = new Machine();
        machine.setUserId(machineCreateDto.getUserId());
        machine.setName(machineCreateDto.getName());
        return machine;
    }

    public MachineDto MachineToMachineDto(Machine machine){

        MachineDto machineDto = new MachineDto();

        machineDto.setId(machine.getId());
        machineDto.setUserId(machine.getUserId());
        machineDto.setStatus(machine.getStatus());
        machineDto.setActive(machine.isActive());
        machineDto.setCreatedAt(machine.getCreatedDate());
        machineDto.setName(machine.getName());

        return machineDto;
    }

    public MachineScheduleErrorDto MachineScheduleErrorToMachineScheduleErrorDto(MachineScheduleError err){

        MachineScheduleErrorDto errDto = new MachineScheduleErrorDto();

        errDto.setUserId(err.getUserId());
        errDto.setMachineId(err.getMachineId());
        errDto.setAction(err.getAction());
        errDto.setDateError(err.getDateError());
        errDto.setMessage(err.getMessage());

        return errDto;
    }

}
