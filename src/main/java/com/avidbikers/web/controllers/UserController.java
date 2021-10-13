package com.avidbikers.web.controllers;

import com.avidbikers.data.model.User;
import com.avidbikers.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/avidbikers/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> getAllUser(){
        return new ResponseEntity<>(List.of(new User("whale", "walker", "whale@gmail.com", "whale123")), HttpStatus.OK);
    }
}
