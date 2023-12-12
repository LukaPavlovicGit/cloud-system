package com.example.usermanagement.data.entities.enums;

import org.springframework.security.core.GrantedAuthority;

public enum PermissionType implements GrantedAuthority{

    CAN_CREATE_USERS("CAN_CREATE_USERS"),
    CAN_READ_USERS("CAN_READ_USERS"),
    CAN_UPDATE_USERS("CAN_UPDATE_USERS"),
    CAN_DELETE_USERS("CAN_DELETE_USERS"),
    CAN_SEARCH_MACHINES("CAN_SEARCH_MACHINES"),
    CAN_START_MACHINES("CAN_START_MACHINES"),
    CAN_STOP_MACHINES("CAN_STOP_MACHINES"),
    CAN_RESTART_MACHINES("CAN_RESTART_MACHINES"),
    CAN_CREATE_MACHINES("CAN_CREATE_MACHINES"),
    CAN_DESTROY_MACHINES("CAN_DESTROY_MACHINES");


    private final String permission;

    PermissionType(String permission) {
        this.permission = permission;
    }

    @Override
    public String getAuthority() {
        return permission;
    }
}
