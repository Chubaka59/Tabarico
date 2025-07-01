package com.gtarp.tabarico.services;

import com.gtarp.tabarico.dto.CheckboxUpdateRequestDto;
import com.gtarp.tabarico.dto.UserDto;
import com.gtarp.tabarico.entities.User;

public interface UserService extends CrudService<User, UserDto> {
    void updateBooleanValue(CheckboxUpdateRequestDto checkboxUpdateRequestDto);
}
