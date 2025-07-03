package com.gtarp.tabarico.services.impl;

import com.gtarp.tabarico.dto.CheckboxUpdateRequestDto;
import com.gtarp.tabarico.dto.UserDto;
import com.gtarp.tabarico.entities.User;
import com.gtarp.tabarico.exception.UserAlreadyExistException;
import com.gtarp.tabarico.exception.UserNotFoundException;
import com.gtarp.tabarico.repositories.UserRepository;
import com.gtarp.tabarico.services.AbstractCrudService;
import com.gtarp.tabarico.services.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl extends AbstractCrudService<User, UserRepository, UserDto> implements UserService {

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

    public void updateBooleanValue(CheckboxUpdateRequestDto checkboxUpdateRequestDto) {
        User user = getById(checkboxUpdateRequestDto.getUserId());
        switch (checkboxUpdateRequestDto.getFieldName()) {
            case "quota":
                user.setQuota(checkboxUpdateRequestDto.isNewValue());
                break;

            case "exporterQuota":
                user.setExporterQuota(checkboxUpdateRequestDto.isNewValue());
                break;

            case "holiday":
                user.setHoliday(checkboxUpdateRequestDto.isNewValue());
                break;

            case "warning1":
                user.setWarning1(checkboxUpdateRequestDto.isNewValue());
                break;

            case "warning2":
                user.setWarning2(checkboxUpdateRequestDto.isNewValue());
                break;
        }
        this.repository.save(user);
    }

    public User getByUsername(String username) {
        return this.repository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public User updatePassword(Integer id, UserDto userDto) {
        User updatedEntity = getById(id).updatePassword(userDto);
        return repository.save(updatedEntity);
    }
}
