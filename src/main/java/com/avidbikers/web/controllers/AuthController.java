package com.avidbikers.web.controllers;

import com.avidbikers.data.dto.UserDto;
import com.avidbikers.data.model.User;
import com.avidbikers.services.AuthService;
import com.avidbikers.web.exceptions.AuthException;
import com.avidbikers.web.payload.ApiResponse;
import com.avidbikers.web.payload.AuthenticationDetails;
import com.avidbikers.web.payload.LoginDto;
import com.avidbikers.web.payload.PasswordRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/avidbikers/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDto userDto) {
        try {
            User savedUser = authService.register(userDto);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (AuthException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        try {
            AuthenticationDetails authenticationDetail = authService.login(loginDto);
            return new ResponseEntity<>(authenticationDetail, HttpStatus.OK);
        } catch (AuthException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/password/update")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody PasswordRequest passwordRequest){
        try {
            authService.updatePassword(passwordRequest);
            return new ResponseEntity<>(new ApiResponse(true, "User password is successfully updated"), HttpStatus.OK);
        }catch (AuthException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
