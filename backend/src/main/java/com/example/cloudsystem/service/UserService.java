package com.example.cloudsystem.service;

import com.example.cloudsystem.data.dto.UserCreateDto;
import com.example.cloudsystem.data.dto.UserDto;
import com.example.cloudsystem.data.dto.UserUpdateDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends UserDetailsService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto getUserByEmail(String email);
    UserDto createUser(UserCreateDto createUserDto);
    UserDto updateUser(UserUpdateDto updateUserDto);
    void deleteUser(Long id);
    void deleteUser(String email);
}
