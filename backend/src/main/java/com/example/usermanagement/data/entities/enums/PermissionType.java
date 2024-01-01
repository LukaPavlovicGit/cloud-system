package com.example.usermanagement.data.entities.enums;

import org.springframework.security.core.GrantedAuthority;

public enum PermissionType implements GrantedAuthority {

    CAN_CREATE_USERS("CAN_CREATE_USERS"),
    CAN_READ_USERS("CAN_READ_USERS"),
    CAN_UPDATE_USERS("CAN_UPDATE_USERS"),
    CAN_DELETE_USERS("CAN_DELETE_USERS"),
    CAN_SEARCH_VACUUM("CAN_SEARCH_VACUUM"),
    CAN_START_VACUUM("CAN_START_VACUUM"),
    CAN_STOP_VACUUM("CAN_STOP_VACUUM"),
    CAN_DISCHARGE_VACUUM("CAN_DISCHARGE_VACUUM"),
    CAN_CREATE_VACUUM("CAN_CREATE_VACUUM"),
    CAN_DESTROY_VACUUM("CAN_DESTROY_VACUUM");


    private final String permission;

    PermissionType(String permission) {
        this.permission = permission;
    }

    @Override
    public String getAuthority() {
        return permission;
    }
}
