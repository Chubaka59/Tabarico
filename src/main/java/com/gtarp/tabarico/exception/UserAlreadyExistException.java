package com.gtarp.tabarico.exception;

public class UserAlreadyExistException extends ConflictException {
    public UserAlreadyExistException(String username) {
        super("User with username " + username + " already exists");
    }
}
