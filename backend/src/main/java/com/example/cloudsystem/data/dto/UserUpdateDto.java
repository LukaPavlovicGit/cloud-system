package com.example.cloudsystem.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDto {

    @NotNull
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<String> permissions;
}
