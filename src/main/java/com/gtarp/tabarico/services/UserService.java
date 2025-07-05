package com.gtarp.tabarico.services;

import com.gtarp.tabarico.dto.CheckboxUpdateRequestDto;
import com.gtarp.tabarico.dto.UserDto;
import com.gtarp.tabarico.entities.User;

import java.util.Map;

public interface UserService extends CrudService<User, UserDto> {
    void updateBooleanValue(CheckboxUpdateRequestDto checkboxUpdateRequestDto);
    User getByUsername(String username);
    User updatePassword(Integer id, UserDto userDto);
    void updateHoliday(Map<String, Object> payload);
    User disableHolidayWhenExpire(User user);
}
