package com.gtarp.tabarico.exception;

public class RoleNotFoundException extends NotFoundException {
    public RoleNotFoundException(int id) { super("Role with id " + id + " not found"); }
    public RoleNotFoundException(String name) { super("Role with the name " + name + " not found"); }
}
