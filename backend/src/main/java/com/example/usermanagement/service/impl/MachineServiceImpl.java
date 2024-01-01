package com.example.usermanagement.service.impl;

import com.example.usermanagement.data.dto.*;
import com.example.usermanagement.data.entities.Machine;
import com.example.usermanagement.data.entities.MachineScheduleError;
import com.example.usermanagement.data.entities.MachineSchedule;
import com.example.usermanagement.data.entities.enums.MachineAction;
import com.example.usermanagement.data.entities.enums.MachineStatus;
import com.example.usermanagement.data.mapper.MachineMapper;
import com.example.usermanagement.exceptions.MachineException;
import com.example.usermanagement.exceptions.NotFoundException;
import com.example.usermanagement.repositories.MachineScheduleErrorRepository;
import com.example.usermanagement.repositories.MachineRepository;
import com.example.usermanagement.repositories.MachineScheduleRepository;
import com.example.usermanagement.service.MachineService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MachineServiceImpl implements MachineService {

    private static final Logger logger = LoggerFactory.getLogger(MachineServiceImpl.class);

    private final MachineRepository machineRepository;
    private final MachineScheduleRepository machineScheduleRepository;
    private final MachineScheduleErrorRepository machineScheduleErrorRepository;
    private final MachineMapper machineMapper;
    private final RabbitTemplate rabbitTemplate;


    public MachineServiceImpl(MachineRepository machineRepository,
                              MachineScheduleRepository machineScheduleRepository,
                              MachineScheduleErrorRepository machineScheduleErrorRepository,
                              MachineMapper machineMapper,
                              RabbitTemplate rabbitTemplate
    ){
        this.machineRepository = machineRepository;
        this.machineScheduleRepository = machineScheduleRepository;
        this.machineScheduleErrorRepository = machineScheduleErrorRepository;
        this.machineMapper = machineMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    @Override
    public List<MachineDto> searchMachines(Long userId, String name, List<MachineStatus> statuses, Long from, Long to) {

        name = (name == null || name.isEmpty()) ? null : name;
        Date fromDate = (from == null) ? null : Date.from(Instant.ofEpochMilli(from));
        Date toDate = (to == null) ? null : Date.from(Instant.ofEpochMilli(to));
        statuses = (statuses == null || statuses.isEmpty()) ? null : statuses;

        String toPrt = "null";

        if(statuses != null){
            toPrt = statuses.toString();
        }

        logger.info("name: " + name + ", statuses " + toPrt +  ", fromDate " + fromDate + ", toDate: " + toDate);

        List<Machine> machines = machineRepository.findAllByCriteria(userId, name, statuses, fromDate, toDate);

        return machines.stream().map(machineMapper::MachineToMachineDto).collect(Collectors.toList());
    }

    @Override
    public MachineDto getMachineById(Long id) {
        Optional<Machine> optionalMachine = machineRepository.findById(id);
        return optionalMachine.map(machineMapper::MachineToMachineDto).orElse(null);
    }

    @Transactional
    @Override
    public MachineDto createMachine(MachineCreateDto machineCreateDto) {
        Machine machine = machineMapper.MachineCreateDtoToMachine(machineCreateDto);
        return machineMapper.MachineToMachineDto(machineRepository.save(machine));
    }

    @Transactional
    @Override
    public void scheduleMachineStart(MachineScheduleDto machineScheduleDto) {
        MachineSchedule machineSchedule = new MachineSchedule();
        machineSchedule.setUserId(machineScheduleDto.getUserId());
        machineSchedule.setScheduleDate(Date.from(Instant.ofEpochMilli(machineScheduleDto.getScheduleDate())));
        machineSchedule.setMachineId(machineScheduleDto.getMachineId());
        machineSchedule.setTargetAction(MachineAction.START);
        machineScheduleRepository.save(machineSchedule);
    }

    @Transactional
    @Override
    public void scheduleMachineStop(MachineScheduleDto machineScheduleDto) {
        MachineSchedule machineSchedule = new MachineSchedule();
        machineSchedule.setUserId(machineScheduleDto.getUserId());
        machineSchedule.setScheduleDate(Date.from(Instant.ofEpochMilli(machineScheduleDto.getScheduleDate())));
        machineSchedule.setMachineId(machineScheduleDto.getMachineId());
        machineSchedule.setTargetAction(MachineAction.STOP);
        machineScheduleRepository.save(machineSchedule);
    }

    @Transactional
    @Override
    public void scheduleMachineDischarge(MachineScheduleDto machineScheduleDto) {
        MachineSchedule machineSchedule = new MachineSchedule();
        machineSchedule.setUserId(machineScheduleDto.getUserId());
        machineSchedule.setScheduleDate(Date.from(Instant.ofEpochMilli(machineScheduleDto.getScheduleDate())));
        machineSchedule.setMachineId(machineScheduleDto.getMachineId());
        machineSchedule.setTargetAction(MachineAction.DISCHARGE);
        machineScheduleRepository.save(machineSchedule);
    }

    @Override
    public List<MachineScheduleErrorDto> getMachineScheduleActionErrors(Long userId) {
        List<MachineScheduleError> errors = machineScheduleErrorRepository.findAllByUserId(userId);
        return errors.stream().map(machineMapper::MachineScheduleErrorToMachineScheduleErrorDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void attemptMachineStart(Long id) {
        Machine machine = getMachine(id);
        checkMachineConditions(machine, getRequiredMachineStatusForTargetAction(MachineAction.START));
        performMachineAction(machine, MachineAction.START);
    }

    @Transactional
    @Override
    public void attemptMachineStop(Long id) {
        Machine machine = getMachine(id);
        checkMachineConditions(machine, getRequiredMachineStatusForTargetAction(MachineAction.STOP));
        performMachineAction(machine, MachineAction.STOP);
    }

    @Transactional
    @Override
    public void attemptMachineDischarge(Long id) {
        Machine machine = getMachine(id);
        checkMachineConditions(machine, getRequiredMachineStatusForTargetAction(MachineAction.DISCHARGE));
        performMachineAction(machine, MachineAction.DISCHARGE);
    }

    @Override
    public void attemptMachineDestroy(Long id) {
        Machine machine = getMachine(id);
        if(machine.getStatus() != MachineStatus.STOPPED){
            throw new MachineException("Machine needs to be in STOPPED state in order to DESTROY it");
        }
        machine.setActive(false);
        machineRepository.save(machine);
    }

    @Transactional
    public void machineStart(Long machineId) throws InterruptedException {
        Machine machine = machineRepository.findById(machineId).orElseThrow(() -> new NotFoundException("invalid machine id"));
        logger.info(String.format("Machine with id:%d is STARTING", machineId));
        Thread.sleep(1000);
        machine.setStatus(MachineStatus.RUNNING);
        machine.setBusy(false);
        machine = machineRepository.saveAndFlush(machine);
        logger.info(String.format("Machine with id:%d has been STARTED successfully", machineId));
    }
    @Transactional
    public void machineStop(Long machineId) throws InterruptedException {
        Machine machine = machineRepository.findById(machineId).orElseThrow(() -> new NotFoundException("invalid machine id"));
        logger.info(String.format("Machine with id:%d is STOPPING", machineId));
        Thread.sleep(1000);
        machine.setStatus(MachineStatus.STOPPED);
        machine.setRunningStopCycles(machine.getRunningStopCycles() + 1);
        if(machine.getRunningStopCycles() % 3 == 0){
            machine = machineRepository.saveAndFlush(machine);
            sendToQueue(machine.getId(), MachineAction.DISCHARGE);
            logger.info(String.format("Machine with id:%d has been STOPPED successfully and sent to DISCHARGE", machineId));
        } else {
            machine.setBusy(false);
            machineRepository.saveAndFlush(machine);
            logger.info(String.format("Machine with id:%d has been STOPPED successfully", machineId));
        }
    }
    @Transactional
    public void machineDischarge(Long machineId) throws InterruptedException {
        Machine machine = machineRepository.findById(machineId).orElseThrow(() -> new NotFoundException("invalid machine id"));
        logger.info(String.format("DISCHARGING for the machine with id:%d starts...", machineId));
        Thread.sleep(1000);
        logger.info(String.format("Machine with id:%d is DISCHARGING", machineId));
        machine.setStatus(MachineStatus.DISCHARGING);
        machine = machineRepository.saveAndFlush(machine);
        Thread.sleep(1000);
        machine.setStatus(MachineStatus.STOPPED);
        machine.setBusy(false);
        machineRepository.saveAndFlush(machine);
        logger.info(String.format("Machine with id:%d has been DISCHARGED successfully", machineId));
    }

    private Machine getMachine(Long id) {
        return machineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Machine with id: %s not found", id)));
    }

    private void checkMachineConditions(Machine machine, MachineStatus requiredStatus) {
        if(machine.isBusy()) {
            throw new MachineException("Machine is busy");
        }
        if(!machine.isActive()) {
            throw new MachineException(String.format("Machine with id %s is not active", machine.getId()));
        }
        if(machine.getStatus() != requiredStatus) {
            throw new MachineException(String.format("Machine needs to be in %s state", requiredStatus));
        }
    }

    private void  performMachineAction(Machine machine, MachineAction action) {
        try {
            machine.setBusy(true);
            machineRepository.saveAndFlush(machine);
            sendToQueue(machine.getId(), action);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new MachineException("Someone has overtaken you in action");
        }
    }

    private MachineStatus getRequiredMachineStatusForTargetAction(MachineAction targetAction){
        return switch (targetAction) {
            case START, DISCHARGE -> MachineStatus.STOPPED;
            case STOP -> MachineStatus.RUNNING;
        };
    }

    private void sendToQueue(Long id, MachineAction machineAction) {
        MachineQueueDto machineQueueDto = new MachineQueueDto();
        machineQueueDto.setMachineId(id);
        machineQueueDto.setMachineAction(machineAction);
        rabbitTemplate.convertAndSend("machineQueue", machineQueueDto);
    }

    @Transactional(dontRollbackOn = MachineException.class)
    @Scheduled(cron = "0 * * * * *")  //every minute
    @SchedulerLock(name = "machineTasksScheduler")
    public void executeScheduledTasks(){
        logger.info("Scheduled job starts...");

        Date date = Date.from(Instant.now().plusSeconds( 2 * 60));
        List<MachineSchedule> machineSchedules = machineScheduleRepository.findAllByScheduleDateBefore(date);

        for(MachineSchedule machineSchedule: machineSchedules){
            Long userId = machineSchedule.getUserId();
            Long machineId = machineSchedule.getMachineId();
            MachineAction targetAction = machineSchedule.getTargetAction();

            try {
                logger.info(String.format("Machine id: %s. Attempting scheduled action: %s", machineId, targetAction));
                switch (targetAction){
                    case START: attemptMachineStart(machineId); break;
                    case STOP: attemptMachineStop(machineId); break;
                    case DISCHARGE: attemptMachineDischarge(machineId); break;
                }
            }
            catch (MachineException e) {
                logger.info(String.format("Machine id: %s. Attempting scheduled action: %s Unsuccessful", machineId, targetAction));
                MachineScheduleError scheduleError = new MachineScheduleError();
                scheduleError.setUserId(userId);
                scheduleError.setMessage(e.getMessage());
                scheduleError.setAction(targetAction);
                scheduleError.setMachineId(machineId);
                scheduleError.setDateError(date);
                machineScheduleErrorRepository.saveAndFlush(scheduleError);
            }
        }
        machineScheduleRepository.deleteAll(machineSchedules);
    }

}
