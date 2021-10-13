package com.avidbikers.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
public class User {
    @Id
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
