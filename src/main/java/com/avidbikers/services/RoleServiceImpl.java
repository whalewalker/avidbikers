package com.avidbikers.services;

import com.avidbikers.data.model.Role;
import com.avidbikers.data.repository.RoleRepository;
import com.avidbikers.web.exceptions.UserRoleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Override
    public Role findByName(String name)  {
        return findRoleByName(name);
    }

    @Override
    public List<Role> getAllRoles() {
        return getAllRolesInDb();
    }

    private List<Role> getAllRolesInDb() {
        return roleRepository.findAll();
    }

    private Role findRoleByName(String name) {
        return roleRepository.findRoleByName(name)
                .orElseThrow(() -> new UsernameNotFoundException("Not find role with role name " + name));
    }

    @Override
    public void createNewRole(Role role) {
        createANewRole(role);
    }

    private void createANewRole(Role role) {
        roleRepository.save(role);
    }
}
