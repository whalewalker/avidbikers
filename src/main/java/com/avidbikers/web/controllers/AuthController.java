package com.avidbikers.web.controllers;

import com.avidbikers.data.dto.BuyerDto;
import com.avidbikers.data.model.Token;
import com.avidbikers.data.model.User;
import com.avidbikers.services.AuthService;
import com.avidbikers.web.exceptions.AuthException;
import com.avidbikers.web.exceptions.TokenException;
import com.avidbikers.web.payload.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/v1/avidbikers/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid BuyerDto buyerDto) {
        log.info("Reached => {}", buyerDto);
        try {
            User savedUser = authService.registerBuyer(buyerDto);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (AuthException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login( @RequestBody @Valid LoginDto loginDto) {
        try {
            AuthenticationDetails authenticationDetail = authService.login(loginDto);
            return new ResponseEntity<>(authenticationDetail, HttpStatus.OK);
        } catch (AuthException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/password/update")
    public ResponseEntity<?> updatePassword( @RequestBody @Valid PasswordRequest passwordRequest){
        try {
            authService.updatePassword(passwordRequest);
            return new ResponseEntity<>(new ApiResponse(true, "User password is successfully updated"), HttpStatus.OK);
        }catch (AuthException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/password/reset/{username}")
    public ResponseEntity<?> forgotPassword(@PathVariable String username){
        try {
            Token passwordResetToken = authService.generatePasswordResetToken(username);
            return new ResponseEntity<>(passwordResetToken, HttpStatus.CREATED  );
        }catch (AuthException exception){
            return new ResponseEntity<>(new ApiResponse(false, exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/password/reset/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable String token, @Valid  @RequestBody PasswordResetRequest request){
        try{
            authService.resetUserPassword(request, token);
            return new ResponseEntity<>(new ApiResponse(true, "Password reset is successful"), HttpStatus.OK);
        }catch (AuthException | TokenException exception){
            return new ResponseEntity<>(new ApiResponse(false, exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
