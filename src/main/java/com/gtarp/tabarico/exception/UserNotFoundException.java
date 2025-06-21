package com.gtarp.tabarico.exception;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(int id) { super("User with id " + id + " not found"); }
    public UserNotFoundException(String username) { super("User with username " + username + " not found"); }
}
