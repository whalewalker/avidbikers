package com.avidbikers.web.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDto {
    @Email(message = "Invalid email")
    private String email;

    @Size(min = 6, max = 20, message = "Invalid password")
    private String password;
}
