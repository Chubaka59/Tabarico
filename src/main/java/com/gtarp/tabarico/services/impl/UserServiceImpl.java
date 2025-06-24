package com.gtarp.tabarico.services.impl;

import com.gtarp.tabarico.dto.UserDto;
import com.gtarp.tabarico.entities.User;
import com.gtarp.tabarico.exception.UserAlreadyExistException;
import com.gtarp.tabarico.exception.UserNotFoundException;
import com.gtarp.tabarico.repositories.UserRepository;
import com.gtarp.tabarico.services.AbstractCrudService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl extends AbstractCrudService<User, UserRepository, UserDto> {

    public UserServiceImpl(UserRepository repository) {
        super(repository);
    }


    @Override
    public User getById(Integer id) {
        return this.repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User insert(UserDto userDto) {
        Optional<User> existingUser = this.repository.findUserByUsername(userDto.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistException(userDto.getUsername());
        }
        User newUser = new User(userDto);
        return this.repository.save(newUser);
    }
}
