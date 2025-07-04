package com.gtarp.tabarico.controllers;

import com.gtarp.tabarico.dto.CheckboxUpdateRequestDto;
import com.gtarp.tabarico.dto.RoleDto;
import com.gtarp.tabarico.dto.UserDto;
import com.gtarp.tabarico.entities.Role;
import com.gtarp.tabarico.entities.User;
import com.gtarp.tabarico.exception.UserNotFoundException;
import com.gtarp.tabarico.services.CrudService;
import com.gtarp.tabarico.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    private Model model;
    @Mock
    private BindingResult result;
    @Mock
    private Principal principal;
    @Mock
    private UserService userService;
    @Mock
    private CrudService<Role, RoleDto> roleService;
    @Mock
    private AccountingController accountingController;

    @Test
    public void getUserListPageTest() {
        //GIVEN we should get this string and a list of users
        String expectedString = "userList";
        when(userService.getAll()).thenReturn(new ArrayList<>());

        //WHEN we call the methode
        String actualString = userController.getUserListPage(model);

        //THEN we get the correct return and the list is called
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).getAll();
    }

    @Test
    public void showAddUserPageTest() {
        //GIVEN we should get this string
        String expectedString = "addUser";
        when(roleService.getAll()).thenReturn(new ArrayList<>());

        //WHEN we call the method
        String actualString = userController.showAddUserPage(new UserDto(), model);

        //THEN we get the correct return
        assertEquals(expectedString, actualString);
    }

    @Test
    public void addUserTest() {
        //GIVEN we would add a user
        String expectedString = "userList";
        when(result.hasErrors()).thenReturn(false);
        when(userService.insert(any(UserDto.class))).thenReturn(new User());
        when(userService.getAll()).thenReturn(new ArrayList<>());

        //WHEN we try to add a user
        String actualString = userController.addUser(new UserDto(), result, model);

        //THEN we get the correct string and the user is added
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).insert(any(UserDto.class));
    }

    @Test
    public void addUserWhenErrorInTheFormTest() {
        //GIVEN there is an error in the form
        String expectedString = "addUser";
        when(result.hasErrors()).thenReturn(true);

        //WHEN we try to add a user
        String actualString = userController.addUser(new UserDto(), result, model);

        //THEN we get the correct string and the user is not created
        assertEquals(expectedString, actualString);
        verify(userService, times(0)).insert(any(UserDto.class));
    }

    @Test
    public void addUserWhenAnExceptionIsThrownTest() {
        //GIVEN an exception will be thrown
        String expectedString = "addUser";
        when(userService.insert(any(UserDto.class))).thenThrow(new RuntimeException());

        //WHEN we try to add a user
        String actualString = userController.addUser(new UserDto(), result, model);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).insert(any(UserDto.class));
    }

    @Test
    public void deleteUserTest() {
        //GIVEN we would try to delete a user
        String expectedString = "userList";
        doNothing().when(userService).delete(anyInt());

        //WHEN we try to delete a user
        String actualString = userController.deleteUser(anyInt(), model);

        //THEN the user is deleted and we get the correct string
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).delete(anyInt());
    }

    @Test
    public void showUpdatePageTest() {
        //GIVEN we should get this string and a user to update
        String expectedString = "updateUser";
        when(userService.getById(anyInt())).thenReturn(new User());
        when(roleService.getAll()).thenReturn(new ArrayList<>());

        //WHEN we call this method
        String actualString = userController.showUpdateUserPage(anyInt(), model);

        //THEN we get the correct string and a user to update
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).getById(anyInt());
    }

    @Test
    public void updateUserTest() {
        String expectedString = "userList";
        when(userService.update(anyInt(), any(UserDto.class))).thenReturn(new User());
        when(result.hasErrors()).thenReturn(false);

        //WHEN we call this method
        String actualString = userController.updateUser(1, new UserDto(), result, model);

        //THEN we get the correct string and the user is updated
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).update(anyInt(), any(UserDto.class));
    }

    @Test
    public void updateUserWhenErrorIsThrownTest() {
        //GIVEN an esxception should be thrown
        String expectedString = "updateUser";
        when(userService.update(anyInt(), any(UserDto.class))).thenThrow(new RuntimeException());
        when(result.hasErrors()).thenReturn(false);

        //WHEN we try to update a user
        String actualString = userController.updateUser(1, new UserDto(), result, model);

        //THEN we get the correct String
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).update(anyInt(), any(UserDto.class));
    }

    @Test
    public void updateUserWhenErrorInTheFormTest() {
        String expectedString = "updateUser";
        when(result.hasErrors()).thenReturn(true);

        //WHEN we try to update the user
        String actualString = userController.updateUser(1, new UserDto(), result, model);

        //THEN we get the correct string and we don't update the user
        assertEquals(expectedString, actualString);
        verify(userService, times(0)).update(anyInt(), any(UserDto.class));
    }

    @Test
    public void updateBooleanValueTest() {
        //GIVEN we should update the value of a user and get the correct response
        doNothing().when(userService).updateBooleanValue(new CheckboxUpdateRequestDto());

        //WHEN we try to update the value
        ResponseEntity<Void> actualResponse = userController.updateBooleanValue(new CheckboxUpdateRequestDto());

        //THEN we get the correct response and the value has been updated
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        verify(userService, times(1)).updateBooleanValue(any(CheckboxUpdateRequestDto.class));
    }

    @Test
    public void updateBooleanValueWhenUserNotFoundExceptionIsThrownTest() {
        //GIVEN an exception should be thrown
        doThrow(UserNotFoundException.class).when(userService).updateBooleanValue(new CheckboxUpdateRequestDto());

        //WHEN we try to update the value
        ResponseEntity<Void> actualResponse = userController.updateBooleanValue(new CheckboxUpdateRequestDto());

        assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
        verify(userService, times(1)).updateBooleanValue(any(CheckboxUpdateRequestDto.class));
    }

    @Test
    public void updateBooleanValueWhenExceptionIsThrownTest() {
        //GIVEN an exception should be thrown
        doThrow(RuntimeException.class).when(userService).updateBooleanValue(new CheckboxUpdateRequestDto());

        //WHEN we try to update the value
        ResponseEntity<Void> actualResponse = userController.updateBooleanValue(new CheckboxUpdateRequestDto());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
        verify(userService, times(1)).updateBooleanValue(any(CheckboxUpdateRequestDto.class));
    }

    @Test
    public void getResetPasswordPageTest() {
        //GIVEN we should get this string
        String expectedString = "resetPassword";
        when(userService.getByUsername(anyString())).thenReturn(new User());
        when(principal.getName()).thenReturn("test");

        //WHEN we call the method
        String actualString = userController.getResetPasswordPage(model, principal);

        //THEN we get the correct String and the information of the user
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).getByUsername(anyString());
    }

    @Test
    public void resetPasswordTest() {
        String expectedString = "personalDashboard";
        when(userService.updatePassword(anyInt(), any(UserDto.class))).thenReturn(new User());
        when(result.hasErrors()).thenReturn(false);
        when(accountingController.getPersonalDashboardPage(any(Model.class), any(Principal.class))).thenReturn("personalDashboard");

        //WHEN we call this method
        String actualString = userController.resetPassword(1, new UserDto(), result, model, principal);

        //THEN we get the correct string and the user is updated
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).updatePassword(anyInt(), any(UserDto.class));
    }

    @Test
    public void resetPasswordWhenErrorIsThrownTest() {
        //GIVEN an esxception should be thrown
        String expectedString = "resetPassword";
        when(userService.updatePassword(anyInt(), any(UserDto.class))).thenThrow(new RuntimeException());
        when(result.hasErrors()).thenReturn(false);

        //WHEN we try to update a user
        String actualString = userController.resetPassword(1, new UserDto(), result, model, principal);

        //THEN we get the correct String
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).updatePassword(anyInt(), any(UserDto.class));
    }

    @Test
    public void resetPasswordWhenErrorInTheFormTest() {
        String expectedString = "resetPassword";
        when(result.hasErrors()).thenReturn(true);

        //WHEN we try to update the user
        String actualString = userController.resetPassword(1, new UserDto(), result, model, principal);

        //THEN we get the correct string and we don't update the user
        assertEquals(expectedString, actualString);
        verify(userService, times(0)).updatePassword(anyInt(), any(UserDto.class));
    }

    @Test
    public void updateHolidayDataTest() {
        //GIVEN we should update the holiday datas and get a response
        doNothing().when(userService).updateHoliday(any(HashMap.class));

        //WHEN we try to update the data
        ResponseEntity<Void> actualResponse = userController.updateHolidayData(new HashMap());

        //THEN we get the correct response and the data has been updated
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        verify(userService, times(1)).updateHoliday(any(HashMap.class));
    }

    @Test
    public void updateHolidayDataWhenUserNotFoundExceptionIsThrownTest() {
        //GIVEN an exception should be thrown
        doThrow(UserNotFoundException.class).when(userService).updateHoliday(any(HashMap.class));

        //WHEN we try to update the value
        ResponseEntity<Void> actualResponse = userController.updateHolidayData(new HashMap<>());

        assertEquals(HttpStatus.NOT_FOUND, actualResponse.getStatusCode());
        verify(userService, times(1)).updateHoliday(any(HashMap.class));
    }

    @Test
    public void updateHolidayDataWhenExceptionIsThrownTest() {
        //GIVEN an exception should be thrown
        doThrow(RuntimeException.class).when(userService).updateHoliday(any(HashMap.class));

        //WHEN we try to update the value
        ResponseEntity<Void> actualResponse = userController.updateHolidayData(new HashMap<>());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, actualResponse.getStatusCode());
        verify(userService, times(1)).updateHoliday(any(HashMap.class));
    }
}
