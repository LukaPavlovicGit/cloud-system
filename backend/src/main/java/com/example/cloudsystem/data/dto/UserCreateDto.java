package com.example.cloudsystem.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> permissions;
}
