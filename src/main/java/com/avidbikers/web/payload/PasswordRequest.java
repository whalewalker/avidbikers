package com.avidbikers.web.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class PasswordRequest {
    @Email(message = "Email must be valid")
    private String email;

    @Size(min = 6, max = 20, message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "password cannot be blank")
    private String oldPassword;
}
