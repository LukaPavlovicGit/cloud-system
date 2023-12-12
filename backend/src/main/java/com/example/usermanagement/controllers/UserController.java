package com.example.usermanagement.controllers;

import com.example.usermanagement.data.dto.UserCreateDto;
import com.example.usermanagement.data.dto.UserUpdateDto;
import com.example.usermanagement.data.entities.enums.PermissionType;
import com.example.usermanagement.exceptions.CustomException;
import com.example.usermanagement.security.CheckSecurity;
import com.example.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Qualifier(value = "userServiceImpl")
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CheckSecurity(permissions = {PermissionType.CAN_CREATE_USERS})
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateDto createUserDto) {
        try{
            return new ResponseEntity<>(userService.createUser(createUserDto), HttpStatus.CREATED);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @CheckSecurity(permissions = {PermissionType.CAN_READ_USERS})
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try{
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @CheckSecurity(permissions = {PermissionType.CAN_READ_USERS})
    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
        try{
            return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @CheckSecurity(permissions = {PermissionType.CAN_UPDATE_USERS})
    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateDto updateUserDto) {
        try{
            return new ResponseEntity<>(userService.updateUser(updateUserDto), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @CheckSecurity(permissions = {PermissionType.CAN_DELETE_USERS})
    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }

    @CheckSecurity(permissions = {PermissionType.CAN_DELETE_USERS})
    @DeleteMapping("/email/{email}")
    public ResponseEntity<?> deleteUserByEmail(@PathVariable("email") String email) {
        try {
            userService.deleteUser(email);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, e.getHttpStatus());
        }
    }
}
