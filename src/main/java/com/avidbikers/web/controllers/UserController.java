package com.avidbikers.web.controllers;

import com.avidbikers.data.dto.ProductDto;
import com.avidbikers.data.model.User;
import com.avidbikers.services.ProductService;
import com.avidbikers.services.UserService;
import com.avidbikers.web.payload.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("api/v1/avidbikers/users")
public class UserController {

    @Autowired
    private UserService userService;


}
