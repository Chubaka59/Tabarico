package com.gtarp.tabarico.services;

import com.gtarp.tabarico.dto.RoleDto;
import com.gtarp.tabarico.entities.Role;
import com.gtarp.tabarico.exception.RoleAlreadyExistException;
import com.gtarp.tabarico.exception.RoleNotFoundException;
import com.gtarp.tabarico.repositories.RoleRepository;
import com.gtarp.tabarico.services.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class RoleServiceTest {
    @InjectMocks
    private RoleServiceImpl roleService;
    @Mock
    private final RoleRepository roleRepository = mock(RoleRepository.class);

    @Test
    public void getAllRolesTest() {
        //GIVEN this should return a list
        List<Role> expectedRoleList = new ArrayList<>();
        when(roleRepository.findAll()).thenReturn(expectedRoleList);

        //WHEN we call the method
        List<Role> actualRoleList = roleService.getAll();

        //THEN the correct method is called and we get the correct return
        assertEquals(expectedRoleList, actualRoleList);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    public void getRoleByIdTest() {
        //GIVEN this should return a role
        Role expectedRole = new Role();
        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(expectedRole));

        //WHEN we try to get this role
        Role actualRole = roleService.getById(1);

        //THEN roleRepository.findById is called and we get the correct return
        assertEquals(expectedRole, actualRole);
        verify(roleRepository, times(1)).findById(anyInt());
    }

    @Test
    public void getRoleByIdWhenRoleIsNotFoundTest() {
        //GIVEN this should not find a role
        when(roleRepository.findById(anyInt())).thenReturn(Optional.empty());

        //WHEN we try to get this role THEN an exception is thrown
        assertThrows(RoleNotFoundException.class, () -> roleService.getById(1));
    }

    @Test
    public void addRoleTest() {
        //GIVEN the role we would add doesn't exist
        when(roleRepository.findRoleByName(anyString())).thenReturn(Optional.empty());
        RoleDto roleDto = new RoleDto(1,"testName", 10, 10000);
        Role role = new Role();
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        //WHEN we try to add this role
        roleService.insert(roleDto);

        //THEN roleRepository.save is called
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    public void addRoleWhenRoleAlreadyExistsTest() {
        //GIVEN the role we would add already exist
        Role role = new Role();
        RoleDto roleDto = new RoleDto(1,"testName", 10, 10000);
        when(roleRepository.findRoleByName(anyString())).thenReturn(Optional.of(role));

        //WHEN we try to add the role THEN an exception is thrown
        assertThrows(RoleAlreadyExistException.class, () -> roleService.insert(roleDto));
    }

    @Test
    public void updateRoleTest() {
        //GIVEN there is a role to update
        Role existingRole = new Role();
        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(existingRole));
        RoleDto roleDto = new RoleDto(1,"testName", 10, 10000);

        //WHEN we try to update the role
        roleService.update(1, roleDto);

        //THEN roleRepository.save is called
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    public void deleteRoleTest() {
        //GIVEN there is a role to delete
        Role existingRole = new Role();
        when(roleRepository.findById(anyInt())).thenReturn(Optional.of(existingRole));
        doNothing().when(roleRepository).delete(any(Role.class));

        //WHEN we try to delete the role
        roleService.delete(1);

        //THEN roleRepository.delete is called
        verify(roleRepository, times(1)).delete(any(Role.class));
    }
}
