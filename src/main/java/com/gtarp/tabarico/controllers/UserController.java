package com.gtarp.tabarico.controllers;

import com.gtarp.tabarico.dto.CheckboxUpdateRequestDto;
import com.gtarp.tabarico.dto.RoleDto;
import com.gtarp.tabarico.dto.UserDto;
import com.gtarp.tabarico.entities.Role;
import com.gtarp.tabarico.exception.UserNotFoundException;
import com.gtarp.tabarico.services.CrudService;
import com.gtarp.tabarico.services.UserService;
import com.gtarp.tabarico.validation.OnResetPassword;
import com.gtarp.tabarico.validation.OnUpdate;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private CrudService<Role, RoleDto> roleService;
    @Autowired
    private AccountingController accountingController;

    @GetMapping("/users")
    public String getUserListPage(Model model) {
        model.addAttribute("users", userService.getAll());
        return "userList";
    }

    @GetMapping("/addUser")
    public String showAddUserPage(UserDto userDto, Model model) {
        model.addAttribute("roles", roleService.getAll());
        return "addUser";
    }

    @PostMapping("/users")
    public String addUser(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("users", userDto);
            return showAddUserPage(userDto, model);
        }
        try {
            userService.insert(userDto);
            return getUserListPage(model);
        } catch (Exception e) {
            return showAddUserPage(userDto, model);
        }
    }

    @GetMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        userService.delete(id);
        return getUserListPage(model);
    }

    @GetMapping("/users/{id}")
    public String showUpdateUserPage(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("userDto", userService.getById(id));
        model.addAttribute("roles", roleService.getAll());
        return "updateUser";
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Validated(OnUpdate.class) @ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "updateUser";
        }
        try {
            userService.update(id, userDto);
            return getUserListPage(model);
        } catch (Exception e) {
            return showUpdateUserPage(id, model);
        }
    }

    @PostMapping("/updateBoolean")
    public ResponseEntity<Void> updateBooleanValue(@RequestBody CheckboxUpdateRequestDto checkboxUpdateRequestDto) {
        try {
            userService.updateBooleanValue(checkboxUpdateRequestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/updateHolidayData")
    public ResponseEntity<Void> updateHolidayData(@RequestBody Map<String, Object> payload) {
        try {
            userService.updateHoliday(payload);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/resetPassword")
    public String getResetPasswordPage(Model model, Principal principal) {
        model.addAttribute("userDto", userService.getByUsername(principal.getName()));
        return "resetPassword";
    }

    @PostMapping("/users/{id}/resetPassword")
    public String resetPassword(@PathVariable("id") Integer id, @Validated(OnResetPassword.class) @ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model, Principal principal) {
        if (result.hasErrors()) {
            model.addAttribute("userDto", userDto);
            return "resetPassword";
        }
        try {
            userService.updatePassword(id, userDto);
            return accountingController.getPersonalDashboardPage(model, principal);
        } catch (Exception e) {
            return getResetPasswordPage(model, principal);
        }
    }
}
