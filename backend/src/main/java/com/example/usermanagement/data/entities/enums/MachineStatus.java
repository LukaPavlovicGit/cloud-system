package com.example.usermanagement.data.entities.enums;

public enum MachineStatus {

    STOPPED("STOPPED"),

    RUNNING("RUNNING");

    private final String status;

    MachineStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
