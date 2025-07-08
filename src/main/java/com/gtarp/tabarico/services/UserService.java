package com.gtarp.tabarico.services;

import com.gtarp.tabarico.dto.CheckboxUpdateRequestDto;
import com.gtarp.tabarico.dto.HolidayModificationDto;
import com.gtarp.tabarico.dto.UserDto;
import com.gtarp.tabarico.entities.User;

public interface UserService extends CrudService<User, UserDto> {
    void updateBooleanValue(CheckboxUpdateRequestDto checkboxUpdateRequestDto);
    User getByUsername(String username);
    User updatePassword(Integer id, UserDto userDto);
    void updateHoliday(HolidayModificationDto holidayModificationDto);
    User disableHolidayWhenExpire(User user);
}
