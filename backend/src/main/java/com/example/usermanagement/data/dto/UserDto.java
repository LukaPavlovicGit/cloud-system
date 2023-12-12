package com.example.usermanagement.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private Long password;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> permissions;
}
