package com.gtarp.tabarico.services;

import com.gtarp.tabarico.dto.UserDto;
import com.gtarp.tabarico.entities.User;

import java.util.List;

public interface UserService {
    /**
     * get a list of all users
     * @return a list of users
     */
    List<User> getAllUsers();

    /**
     * get a user by its id
     * @param id the id of the user
     * @return a user
     */
    User getUserById(int id);

    /**
     * get a user by its username
     * @param username the username of the user
     * @return a user
     */
    User getUserByUsername(String username);

    /**
     * add a user in database
     * @param userDto the information of the user we would add
     * @return the user added
     */
    User addUser(UserDto userDto);

    /**
     * update a user in database
     * @param id the id of the user to update
     * @param userDto the updated information of the user
     * @return the user
     */
    User updateUser(int id, UserDto userDto);

    /**
     * delete a user in database
     * @param id the id of the user to delete
     */
    void deleteUser(int id);
}
