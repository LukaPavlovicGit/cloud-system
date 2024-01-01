package com.example.cloudsystem.service.impl;

import com.example.cloudsystem.data.dto.UserCreateDto;
import com.example.cloudsystem.data.dto.UserDto;
import com.example.cloudsystem.data.dto.UserUpdateDto;
import com.example.cloudsystem.data.entities.Permission;
import com.example.cloudsystem.data.entities.User;
import com.example.cloudsystem.data.entities.enums.PermissionType;
import com.example.cloudsystem.data.mapper.UserMapper;
import com.example.cloudsystem.exceptions.NotFoundException;
import com.example.cloudsystem.repositories.UserRepository;
import com.example.cloudsystem.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserCreateDto userCreateDto) {
        User user = userMapper.UserCreateDtoToUser(userCreateDto);
        return userMapper.UserToUserDto(userRepository.save(user));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::UserToUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {

        Optional<User> user = userRepository.findById(id);
        return user.map(userMapper::UserToUserDto)
                .orElseThrow(() -> new NotFoundException(String.format("No user with id: %s found.", id)));
    }

    @Override
    public UserDto getUserByEmail(String email) {

        Optional<User> user = userRepository.findByEmail(email);
        return user.map(userMapper::UserToUserDto)
                .orElseThrow(() -> new NotFoundException(String.format("No user with email: %s found.", email)));
    }

    @Override
    public UserDto updateUser(UserUpdateDto userUpdateDto) {

        User user = userRepository.findById(userUpdateDto.getId())
                .orElseThrow(() -> new NotFoundException(String.format("No user with id: %s found.", userUpdateDto.getId())));
        user = userMapper.UserUpdateDtoToUser(user, userUpdateDto);
        return userMapper.UserToUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("No user with id: %s found.", id)));
        userRepository.delete(user);
    }

    @Override
    public void deleteUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No user with given email"));
        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()) {
            throw new UsernameNotFoundException("User with email:" + email + ", not found");
        }
        List<PermissionType> permissionTypes = user.get().getPermissions().stream().map(Permission::getPermission).toList();
        return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), permissionTypes);
    }

}
