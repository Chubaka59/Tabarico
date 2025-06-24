package com.gtarp.tabarico.dto;

import com.gtarp.tabarico.entities.Role;
import com.gtarp.tabarico.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    @NotBlank
    @NotBlank(groups = {OnUpdate.class})
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    @NotBlank(groups = {OnUpdate.class})
    private String lastName;
    @NotBlank
    @NotBlank(groups = {OnUpdate.class})
    private String firstName;
    @NotBlank
    @NotBlank(groups = {OnUpdate.class})
    private String phone;
    @NotNull
    @NotNull(groups = {OnUpdate.class})
    private Role role;
}
