package com.gtarp.tabarico.controllers;

import com.gtarp.tabarico.dto.UserDto;
import com.gtarp.tabarico.entities.User;
import com.gtarp.tabarico.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;

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
    private UserService userService;

    @Test
    public void getUserListPageTest() {
        //GIVEN we should get this string and a list of users
        String expectedString = "userList";
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());

        //WHEN we call the methode
        String actualString = userController.getUserListPage(model);

        //THEN we get the correct return and the list is called
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void showAddUserPageTest() {
        //GIVEN we should get this string
        String expectedString = "addUser";

        //WHEN we call the method
        String actualString = userController.showAddUserPage(new UserDto());

        //THEN we get the correct return
        assertEquals(expectedString, actualString);
    }

    @Test
    public void addUserTest() {
        //GIVEN we would add a user
        String expectedString = "userList";
        when(result.hasErrors()).thenReturn(false);
        when(userService.addUser(any(UserDto.class))).thenReturn(new User());
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());

        //WHEN we try to add a user
        String actualString = userController.addUser(new UserDto(), result, model);

        //THEN we get the correct string and the user is added
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).addUser(any(UserDto.class));
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
        verify(userService, times(0)).addUser(any(UserDto.class));
    }

    @Test
    public void addUserWhenAnExceptionIsThrownTest() {
        //GIVEN an exception will be thrown
        String expectedString = "addUser";
        when(userService.addUser(any(UserDto.class))).thenThrow(new RuntimeException());

        //WHEN we try to add a user
        String actualString = userController.addUser(new UserDto(), result, model);

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).addUser(any(UserDto.class));
    }

    @Test
    public void deleteUserTest() {
        //GIVEN we would try to delete a user
        String expectedString = "userList";
        doNothing().when(userService).deleteUser(anyInt());

        //WHEN we try to delete a user
        String actualString = userController.deleteUser(anyInt(), model);

        //THEN the user is deleted and we get the correct string
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).deleteUser(anyInt());
    }

    @Test
    public void showUpdatePageTest() {
        //GIVEN we should get this string and a user to update
        String expectedString = "updateUserPage";
        when(userService.getUserById(anyInt())).thenReturn(new User());

        //WHEN we call this method
        String actualString = userController.showUpdateUserPage(anyInt(), model);

        //THEN we get the correct string and a user to update
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).getUserById(anyInt());
    }

    @Test
    public void updateUserTest() {
        String expectedString = "userList";
        when(userService.updateUser(anyInt(), any(UserDto.class))).thenReturn(new User());
        when(result.hasErrors()).thenReturn(false);

        //WHEN we call this method
        String actualString = userController.updateUser(1, new UserDto(), result, model);

        //THEN we get the correct string and the user is updated
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).updateUser(anyInt(), any(UserDto.class));
    }

    @Test
    public void updateUserWhenErrorIsThrownTest() {
        //GIVEN an esxception should be thrown
        String expectedString = "updateUserPage";
        when(userService.updateUser(anyInt(), any(UserDto.class))).thenThrow(new RuntimeException());
        when(result.hasErrors()).thenReturn(false);

        //WHEN we try to update a user
        String actualString = userController.updateUser(1, new UserDto(), result, model);

        //THEN we get the correct String
        assertEquals(expectedString, actualString);
        verify(userService, times(1)).updateUser(anyInt(), any(UserDto.class));
    }

    @Test
    public void updateUserWhenErrorInTheFormTest() {
        String expectedString = "updateUserPage";
        when(result.hasErrors()).thenReturn(true);

        //WHEN we try to update the user
        String actualString = userController.updateUser(1, new UserDto(), result, model);

        //THEN we get the correct string and we don't update the user
        assertEquals(expectedString, actualString);
        verify(userService, times(0)).updateUser(anyInt(), any(UserDto.class));
    }
}
