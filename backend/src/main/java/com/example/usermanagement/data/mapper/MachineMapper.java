package com.example.usermanagement.data.mapper;

import com.example.usermanagement.data.dto.MachineCreateDto;
import com.example.usermanagement.data.dto.MachineDto;
import com.example.usermanagement.data.entities.Machine;
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
        machineDto.setCreateAt(machine.getCreatedDate());
        machineDto.setName(machine.getName());

        return machineDto;
    }


}
