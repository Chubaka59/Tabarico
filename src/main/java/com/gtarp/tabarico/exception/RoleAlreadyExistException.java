package com.gtarp.tabarico.exception;

public class RoleAlreadyExistException extends ConflictException {
    public RoleAlreadyExistException(String name) {
        super("Role with the name " + name + " already exists");
    }
}
