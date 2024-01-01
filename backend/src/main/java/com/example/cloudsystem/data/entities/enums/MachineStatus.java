package com.example.cloudsystem.data.entities.enums;

public enum MachineStatus {

    STOPPED("STOPPED"),

    RUNNING("RUNNING"),

    DISCHARGING("DISCHARGING");

    private final String status;

    MachineStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }
}
