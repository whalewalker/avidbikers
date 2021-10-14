package com.avidbikers.data.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    @NotBlank(message = "First name can not be blank")
    private String firstName;

    @NotBlank(message = "Last name can not be blank")
    private String lastName;

    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "phone number cannot be blank")
    private String phoneNumber;

    @Size(min = 6, max = 20, message = "Invalid password")
    private String password;
}
