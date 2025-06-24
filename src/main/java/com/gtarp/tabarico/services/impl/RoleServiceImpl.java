package com.gtarp.tabarico.services.impl;

import com.gtarp.tabarico.dto.RoleDto;
import com.gtarp.tabarico.entities.Role;
import com.gtarp.tabarico.exception.RoleAlreadyExistException;
import com.gtarp.tabarico.exception.RoleNotFoundException;
import com.gtarp.tabarico.repositories.RoleRepository;
import com.gtarp.tabarico.services.AbstractCrudService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl extends AbstractCrudService<Role, RoleRepository, RoleDto> {

    public RoleServiceImpl(RoleRepository repository) {
        super(repository);
    }

    @Override
    public Role getById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RoleNotFoundException(id));
    }

    @Override
    public Role insert(RoleDto roleDto) {
        Optional<Role> role = repository.findRoleByName(roleDto.getName());
        if (role.isPresent()) {
            throw new RoleAlreadyExistException(roleDto.getName());
        }
        Role newRole = new Role(roleDto);
        return this.repository.save(newRole);
    }
}
