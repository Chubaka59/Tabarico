package com.gtarp.tabarico.services.impl;

import com.gtarp.tabarico.dto.UserDto;
import com.gtarp.tabarico.entities.User;
import com.gtarp.tabarico.exception.UserAlreadyExistException;
import com.gtarp.tabarico.exception.UserNotFoundException;
import com.gtarp.tabarico.repositories.UserRepository;
import com.gtarp.tabarico.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User addUser(UserDto userDto) {
        Optional<User> existingUser = userRepository.findUserByUsername(userDto.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException(userDto.getUsername());
        }
        User newUser = new User(userDto);
        return userRepository.save(newUser);
    }

    @Override
    public User updateUser(int id, UserDto userDto) {
        User updateduser = getUserById(id).update(userDto);
        return userRepository.save(updateduser);
    }

    @Override
    public void deleteUser(int id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
