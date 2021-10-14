package com.avidbikers.web.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class AuthenticationDetails {
    @NotBlank(message = "Token cannot be null!")
    private String jwtToken;

    @Email(message = "Email must be valid")
    private String email;
}
