package com.avidbikers.data.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
@Data
public class Role {
    @Id
    private String id;
    private String name;
}
