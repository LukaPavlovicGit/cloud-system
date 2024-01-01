package com.example.cloudsystem.controllers;

import com.example.cloudsystem.data.dto.LoginDto;
import com.example.cloudsystem.data.dto.TokenDto;
import com.example.cloudsystem.service.UserService;
import com.example.cloudsystem.utils.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(new TokenDto(jwtUtil.generateToken(userService.getUserByEmail(loginRequest.getEmail()))));
    }
}
