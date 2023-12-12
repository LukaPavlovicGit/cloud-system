package com.example.usermanagement.exceptions;

import org.springframework.http.HttpStatus;

public class MachineException extends CustomException {

    public MachineException(String message){
        super(message, ErrorCode.RESOURCE_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
    }
}
