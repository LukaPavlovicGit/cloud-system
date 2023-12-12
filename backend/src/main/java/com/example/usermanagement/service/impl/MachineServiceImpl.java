package com.example.usermanagement.service.impl;

import com.example.usermanagement.data.dto.MachineCreateDto;
import com.example.usermanagement.data.dto.MachineDto;
import com.example.usermanagement.data.dto.MachineQueueDto;
import com.example.usermanagement.data.dto.MachineScheduleDto;
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

        Date fromDate = (from == null) ? null : Date.from(Instant.ofEpochMilli(from));
        Date toDate = (to == null) ? null : Date.from(Instant.ofEpochMilli(to));
        statuses = (statuses == null || statuses.isEmpty()) ? null : statuses;
        name = (name == null || name.isEmpty()) ? null : name;

        List<Machine> machines = machineRepository.findAllByCriteria(userId, name, statuses, fromDate, toDate);

        return machines.stream().map(machineMapper::MachineToMachineDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public MachineDto createMachine(MachineCreateDto machineCreateDto) {
        Machine machine = machineMapper.MachineCreateDtoToMachine(machineCreateDto);
        return machineMapper.MachineToMachineDto(machineRepository.save(machine));
    }

    @Transactional
    @Override
    public void scheduleStartMachine(MachineScheduleDto machineScheduleDto) {
        MachineSchedule machineSchedule = new MachineSchedule();
        machineSchedule.setScheduleDate(Date.from(Instant.ofEpochMilli(machineScheduleDto.getScheduleDate())));
        machineSchedule.setMachineId(machineScheduleDto.getMachineId());
        machineSchedule.setTargetAction(MachineAction.START);
        machineScheduleRepository.save(machineSchedule);
    }

    @Transactional
    @Override
    public void scheduleStopMachine(MachineScheduleDto machineScheduleDto) {
        MachineSchedule machineSchedule = new MachineSchedule();
        machineSchedule.setScheduleDate(Date.from(Instant.ofEpochMilli(machineScheduleDto.getScheduleDate())));
        machineSchedule.setMachineId(machineScheduleDto.getMachineId());
        machineSchedule.setTargetAction(MachineAction.STOP);
        machineScheduleRepository.save(machineSchedule);
    }

    @Transactional
    @Override
    public void scheduleRestartMachine(MachineScheduleDto machineScheduleDto) {
        MachineSchedule machineSchedule = new MachineSchedule();
        machineSchedule.setScheduleDate(Date.from(Instant.ofEpochMilli(machineScheduleDto.getScheduleDate())));
        machineSchedule.setMachineId(machineScheduleDto.getMachineId());
        machineSchedule.setTargetAction(MachineAction.RESTART);
        machineScheduleRepository.save(machineSchedule);
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
    public void attemptMachineRestart(Long id) {
        Machine machine = getMachine(id);
        checkMachineConditions(machine, getRequiredMachineStatusForTargetAction(MachineAction.RESTART));
        performMachineAction(machine, MachineAction.RESTART);
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
        Thread.sleep(10000);

        try {
            machine.setStatus(MachineStatus.RUNNING);
            machine.setBusy(false);
            machine = machineRepository.saveAndFlush(machine);
            logger.info(String.format("Machine with id:%d has been STARTED successfully", machineId));
        }
        catch (ObjectOptimisticLockingFailureException e) {
            throw new MachineException("someone has overtaken you in action");
        }
    }
    @Transactional
    public void machineStop(Long machineId) throws InterruptedException {
        Machine machine = machineRepository.findById(machineId).orElseThrow(() -> new NotFoundException("invalid machine id"));

        logger.info(String.format("Machine with id:%d is STOPPING", machineId));
        Thread.sleep(10000);

        try {
            machine.setStatus(MachineStatus.STOPPED);
            machine.setBusy(false);
            machine = machineRepository.saveAndFlush(machine);
            logger.info(String.format("Machine with id:%d has been STOPPED successfully", machineId));
        }
        catch (ObjectOptimisticLockingFailureException e) {
            throw new MachineException("someone has overtaken you in action");
        }

    }
    @Transactional
    public void machineRestart(Long machineId) throws InterruptedException {
        Machine machine = machineRepository.findById(machineId).orElseThrow(() -> new NotFoundException("invalid machine id"));

        logger.info(String.format("Machine with id:%d is RESTARTING", machineId));

        try {
            Thread.sleep(5000);
            machine.setStatus(MachineStatus.STOPPED);
            machine = machineRepository.saveAndFlush(machine);
            logger.info(String.format("Machine with id:%d has been STOPPED", machineId));

            Thread.sleep(5000);
            machine.setStatus(MachineStatus.RUNNING);
            machine.setBusy(false);
            machine = machineRepository.saveAndFlush(machine);

            logger.info(String.format("Machine with id:%d has been STARTED again", machineId));
            logger.info(String.format("Machine with id:%d has been RESTARTED successfully", machineId));
        }
        catch (ObjectOptimisticLockingFailureException e) {
            throw new MachineException("someone has overtaken you in action");
        }
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
            case START -> MachineStatus.STOPPED;
            case STOP, RESTART -> MachineStatus.RUNNING;
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
            Long machineId = machineSchedule.getMachineId();
            MachineAction targetAction = machineSchedule.getTargetAction();

            try {
                logger.info(String.format("Machine id: %s. Attempting action: %s", machineId, targetAction));
                switch (targetAction){
                    case START: attemptMachineStart(machineId); break;
                    case STOP: attemptMachineStop(machineId); break;
                    case RESTART: attemptMachineRestart(machineId); break;
                }
            }
            catch (MachineException e) {
                MachineScheduleError scheduleError = new MachineScheduleError();
                scheduleError.setMessage(e.getMessage());
                scheduleError.setAction(targetAction);
                scheduleError.setMachineId(machineId);
                scheduleError.setDateError(date);
                machineScheduleErrorRepository.save(scheduleError);
            }
        }
        machineScheduleRepository.deleteAll(machineSchedules);
    }

}
