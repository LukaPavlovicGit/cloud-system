package com.example.usermanagement.data.mapper;

import com.example.usermanagement.data.dto.UserCreateDto;
import com.example.usermanagement.data.dto.UserDto;
import com.example.usermanagement.data.dto.UserUpdateDto;
import com.example.usermanagement.data.entities.User;
import com.example.usermanagement.data.entities.enums.PermissionType;
import com.example.usermanagement.repositories.PermissionRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    public UserMapper(PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User UserCreateDtoToUser(UserCreateDto userCreateDto){

        User user = new User();
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setPermissions(permissionRepository.findAllByPermissionIn(userCreateDto.getPermissions().stream().map(PermissionType::valueOf).collect(Collectors.toList())));

        return user;
    }

    public User UserUpdateDtoToUser(User user, UserUpdateDto userUpdateDto){

        if(userUpdateDto.getFirstName() != null && !userUpdateDto.getFirstName().isBlank())
            user.setFirstName(userUpdateDto.getFirstName());
        if(userUpdateDto.getLastName() != null && !userUpdateDto.getLastName().isBlank())
            user.setLastName(userUpdateDto.getLastName());
        if(userUpdateDto.getEmail() != null && !userUpdateDto.getEmail().isBlank())
            user.setEmail(userUpdateDto.getEmail());
        if(userUpdateDto.getPassword() != null && !userUpdateDto.getPassword().isBlank())
            user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword()));
        if(userUpdateDto.getPermissions() != null && !userUpdateDto.getPermissions().isEmpty())
            user.setPermissions(permissionRepository.findAllByPermissionIn(userUpdateDto.getPermissions().stream().map(PermissionType::valueOf).collect(Collectors.toList())));

        return user;
    }

    public UserDto UserToUserDto(User user){

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setPermissions(user.getPermissions().stream().map(permission -> permission.getPermission().getAuthority()).toList());

        return userDto;
    }
}
