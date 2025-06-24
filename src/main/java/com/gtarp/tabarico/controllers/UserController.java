package com.gtarp.tabarico.controllers;

import com.gtarp.tabarico.dto.UserDto;
import com.gtarp.tabarico.entities.User;
import com.gtarp.tabarico.services.CrudService;
import com.gtarp.tabarico.validation.PasswordModification;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired
    private CrudService<User, UserDto> userService;

    @GetMapping("/users")
    public String getUserListPage(Model model) {
        model.addAttribute("users", userService.getAll());
        return "userList";
    }

    @GetMapping("/addUser")
    public String showAddUserPage(UserDto userDto) {
        return "addUser";
    }

    @PostMapping("/users")
    public String addUser(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userDto);
            return "addUser";
        }
        try {
            userService.insert(userDto);
            return getUserListPage(model);
        } catch (Exception e) {
            return "addUser";
        }
    }

    @GetMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        userService.delete(id);
        return getUserListPage(model);
    }

    @GetMapping("/users/{id}")
    public String showUpdateUserPage(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", userService.getById(id));
        return "modifyPassword";
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Validated(PasswordModification.class) @ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "modifyPassword";
        }
        try {
            userService.update(id, userDto);
            return getUserListPage(model);
        } catch (Exception e) {
            return "modifyPassword";
        }
    }
}
