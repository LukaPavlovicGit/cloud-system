package com.example.usermanagement.queueListener;

import com.example.usermanagement.data.dto.MachineQueueDto;
import com.example.usermanagement.service.MachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@Component
public class MachineQueueListener {

    private static final Logger logger = LoggerFactory.getLogger(MachineQueueListener.class);

    @Qualifier(value = "machineServiceImpl")
    private final MachineService machineService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public MachineQueueListener(MachineService machineService, SimpMessagingTemplate simpMessagingTemplate) {
        this.machineService = machineService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @RabbitListener(queues = "machineQueue")
    public void machineQueueHandler(Message message) throws InterruptedException, IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(message.getBody());
        ObjectInputStream is = new ObjectInputStream(in);

        MachineQueueDto machineQueueDto = (MachineQueueDto) is.readObject();

        logger.info(String.format("Rabbit listener on the queue:\"%s\". Machine id:%s. Target action:%s", "machineQueue", machineQueueDto.getMachineId(), machineQueueDto.getMachineAction()));

        // Potrebno je ovaj switch staviti u try catch block da bismo uhvatili i sacuvali greske koje se dogode
        switch(machineQueueDto.getMachineAction()) {
            case START: { machineService.machineStart(machineQueueDto.getMachineId()); break; }
            case STOP: { machineService.machineStop(machineQueueDto.getMachineId()); break; }
            case DISCHARGE: { machineService.machineDischarge(machineQueueDto.getMachineId()); break; }
        }
    }
}
