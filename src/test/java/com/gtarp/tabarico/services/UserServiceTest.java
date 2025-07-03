package com.gtarp.tabarico.services;

import com.gtarp.tabarico.dto.CheckboxUpdateRequestDto;
import com.gtarp.tabarico.dto.UserDto;
import com.gtarp.tabarico.entities.Role;
import com.gtarp.tabarico.entities.User;
import com.gtarp.tabarico.exception.UserAlreadyExistException;
import com.gtarp.tabarico.exception.UserNotFoundException;
import com.gtarp.tabarico.repositories.UserRepository;
import com.gtarp.tabarico.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private final UserRepository userRepository = mock(UserRepository.class);

    @Test
    public void getAllUsersTest() {
        //GIVEN this should return a list
        List<User> expectedUserList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(expectedUserList);

        //WHEN we call the method
        List<User> actualUserList = userService.getAll();

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
        User actualUser = userService.getById(1);

        //THEN userRepository.findById is called and we get the correct return
        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).findById(anyInt());
    }

    @Test
    public void getUserByIdWhenUserIsNotFoundTest() {
        //GIVEN this should not find a user
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        //WHEN we try to get this user THEN an exception is thrown
        assertThrows(UserNotFoundException.class, () -> userService.getById(1));
    }

    @Test
    public void addUserTest() {
        //GIVEN the user we would add doesn't exist
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
        UserDto userDto = new UserDto(1, "testUsername", "testPassword", "testLastName", "testFirstName", "testPhone", new Role(), false);
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        //WHEN we try to add this user
        userService.insert(userDto);

        //THEN userRepository.save is called
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void addUserWhenUserAlreadyExistsTest() {
        //GIVEN the user we would add already exist
        User user = new User();
        UserDto userDto = new UserDto(1, "testUsername", "testPassword", "testLastName", "testFirstName", "testPhone", new Role(), false);
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(user));

        //WHEN we try to add the user THEN an exception is thrown
        assertThrows(UserAlreadyExistException.class, () -> userService.insert(userDto));
    }

    @Test
    public void updateUserTest() {
        //GIVEN there is a user to update
        User existingUser = new User();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(existingUser));
        UserDto userDto = new UserDto(1, "testUsername", "testPassword", "testLastName", "testFirstName", "testPhone", new Role(), false);

        //WHEN we try to update the user
        userService.update(1, userDto);

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
        userService.delete(1);

        //THEN userRepository.delete is called
        verify(userRepository, times(1)).delete(any(User.class));
    }


    @ParameterizedTest
    @MethodSource("provideCheckboxUpdateRequestDtoForTest")
    public void updateBooleanValueTest(CheckboxUpdateRequestDto checkboxUpdateRequestDto) {
        //GIVEN we should get a user and save it
        User user = new User(1, "testUsername", "testPassword", "testLastName", "testFirstName", "testPhone", true, new Role(), true, true, true, 30000, 10000, true, true);
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        //WHEN we try to update the boolean value
        userService.updateBooleanValue(checkboxUpdateRequestDto);

        //THEN the user is saved
        verify(userRepository, times(1)).save(any(User.class));
    }

    static Stream<Arguments> provideCheckboxUpdateRequestDtoForTest() {
        return Stream.of(
                Arguments.of(new CheckboxUpdateRequestDto(1, "quota", true)),
                Arguments.of(new CheckboxUpdateRequestDto(1, "exporterQuota", true)),
                Arguments.of(new CheckboxUpdateRequestDto(1, "holiday", true)),
                Arguments.of(new CheckboxUpdateRequestDto(1, "warning1", true)),
                Arguments.of(new CheckboxUpdateRequestDto(1, "warning2", true))
        );
    }

    @Test
    public void getByUsernameTest() {
        //GIVEN we should get a user
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.of(new User()));

        //WHEN we call this method
        userService.getByUsername("testUsername");

        //THEN we get the user
        verify(userRepository, times(1)).findUserByUsername(anyString());
    }

    @Test
    public void getByUsernameWhenUserIsNotFoundTest() {
        //GIVEN this should not find a user
        when(userRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());

        //WHEN we try to get this user THEN an exception is thrown
        assertThrows(UserNotFoundException.class, () -> userService.getById(1));
    }

    @Test
    public void updatePasswordTest() {
        //GIVEN there is a user to update
        User existingUser = new User();
        when(userRepository.findById(anyInt())).thenReturn(Optional.of(existingUser));
        UserDto userDto = new UserDto(1, "testUsername", "testPassword", "testLastName", "testFirstName", "testPhone", new Role(), false);

        //WHEN we try to update the user
        userService.updatePassword(1, userDto);

        //THEN userRepository.save is called
        verify(userRepository, times(1)).save(any(User.class));
    }
}
