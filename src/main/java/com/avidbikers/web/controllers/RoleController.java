package com.avidbikers.web.controllers;

import com.avidbikers.data.model.Role;
import com.avidbikers.services.RoleService;
import com.avidbikers.web.exceptions.UserRoleNotFoundException;
import com.avidbikers.web.payload.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/avidbikers/role")
@Slf4j
public class RoleController {

    @PostConstruct
    private void postConstruct() {
        if (roleService.getAllRoles().isEmpty()) {
            log.info("No Roles found ...");
            Role buyerRole = new Role();
            Role sellerRole = new Role();
            Role adminRole = new Role();
            buyerRole.setName("BUYER");
            sellerRole.setName("SELLER");
            adminRole.setName("ADMIN");
            log.info("Creating buyer role ...");
            roleService.createNewRole(buyerRole);
            log.info("Creating seller role ...");
            roleService.createNewRole(sellerRole);
            log.info("Creating admin role ...");
            roleService.createNewRole(adminRole);
            log.info("Roles created");
        } else {
            log.info("Roles already created...");
        }
    }

    @Autowired
    private RoleService roleService;

    @PostMapping("/new")
    public ResponseEntity<?> newRole(@RequestBody Role role) {
        roleService.createNewRole(role);
        return new ResponseEntity<>(new ApiResponse(true, "Role created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getRoles() {
        List<Role> roles = roleService.getAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("{roleName}")
    public ResponseEntity<?> getRoleByName(@PathVariable String roleName) {
        try {
            Role role = roleService.findByName(roleName);
            return new ResponseEntity<>(role, HttpStatus.OK);
        } catch (UserRoleNotFoundException userRoleNotFoundException) {
            return new ResponseEntity<>(new ApiResponse(false, "No Role found by that name"), HttpStatus.BAD_REQUEST);
        }
    }

}