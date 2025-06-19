package com.gtarp.tabarico.services;

import com.gtarp.tabarico.dto.UserDto;
import com.gtarp.tabarico.entities.User;
import com.gtarp.tabarico.exception.UserAlreadyExistException;
import com.gtarp.tabarico.exception.UserNotFoundException;
import com.gtarp.tabarico.repositories.UserRepository;
import com.gtarp.tabarico.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @MockBean
    private UserService userService;

    private final UserRepository userRepository = mock(UserRepository.class);

    @BeforeEach
    public void setUpPerTest() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void getAllUsersTest() {
        //GIVEN this should return a list
        List<User> expectedUserList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(expectedUserList);

        //WHEN we call the method
        List<User> actualUserList = userService.getAllUsers();

        //THEN the correct method is called and we get the correct return
        assertEquals(expectedUserList, actualUserList);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void getUserByIdTest() {
        //GIVEN this should return a user
        User expectedUser = new User();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(expectedUser));

        //WHEN we try to get this user
        User actualUser = userService.getUserById(1);

        //THEN userRepository.findById is called and we get the correct return
        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    public void getUserByIdWhenUserIsNotFoundTest() {
        //GIVEN this should not find a user
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        //WHEN we try to get this user THEN an exception is thrown
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1));
    }

    @Test
    public void getUserByEmailTest() {
        //GIVEN this should return a user
        User expectedUser = new User();
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(expectedUser));

        //WHEN we try to get this user
        User actualUser = userService.getUserByUsername("testUsername");

        //THEN userRepository.findByUsername is called and we get the correct return
        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).findUserByUsername(anyString());
    }

    @Test
    public void getUserByEmailWhenUserIsNotFoundTest() {
        //GIVEN this should not find a user
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

        //WHEN we try to get this user THEN an exception is thrown
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("testUsername"));
    }

    @Test
    public void addUserTest() {
        //GIVEN the user we would add doesn't exist
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
        UserDto userDto = new UserDto(1, "testUsername", "testPassword", "testLastName", "testFirstName", "testPhone");
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        //WHEN we try to add this user
        userService.addUser(userDto);

        //THEN userRepository.save is called
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void addUserWhenUserAlreadyExistsTest() {
        //GIVEN the user we would add already exist
        User user = new User();
        UserDto userDto = new UserDto(1, "testUsername", "testPassword", "testLastName", "testFirstName", "testPhone");
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

        //WHEN we try to add the user THEN an exception is thrown
        assertThrows(UserAlreadyExistException.class, () -> userService.addUser(userDto));
    }

    @Test
    public void updateUserTest() {
        //GIVEN there is a user to update
        User existingUser = new User();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(existingUser));
        UserDto userDto = new UserDto(1, "testUsername", "testPassword", "testLastName", "testFirstName", "testPhone");

        //WHEN we try to update the user
        userService.updateUser(1, userDto);

        //THEN userRepository.save is called
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void deleteUserTest() {
        //GIVEN there is a user to delete
        User existingUser = new User();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(existingUser));
        doNothing().when(userRepository).delete(any(User.class));

        //WHEN we try to delete the user
        userService.deleteUser(1);

        //THEN userRepository.delete is called
        verify(userRepository, times(1)).delete(any(User.class));
    }
}
