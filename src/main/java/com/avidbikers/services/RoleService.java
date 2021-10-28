package com.avidbikers.services;

import com.avidbikers.data.model.Role;
import com.avidbikers.web.exceptions.UserRoleNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    Role findByName(String name)throws UserRoleNotFoundException;

    List<Role> getAllRoles();

    void createNewRole(Role role);
}
