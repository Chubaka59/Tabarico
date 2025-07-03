package com.gtarp.tabarico.dto;

import com.gtarp.tabarico.entities.Role;
import com.gtarp.tabarico.validation.OnResetPassword;
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
    @NotBlank(message = "Indiquez un nom d'utilisateur")
    @NotBlank(groups = {OnUpdate.class}, message = "Indiquez un nom d'utilisateur")
    private String username;
    @NotBlank(message = "Indiquez un mot de passe")
    @NotBlank(groups = {OnResetPassword.class}, message = "Indiquez un mot de passe")
    private String password;
    @NotBlank(message = "Indiquez un nom")
    @NotBlank(groups = {OnUpdate.class}, message = "Indiquez un nom")
    private String lastName;
    @NotBlank(message = "Indiquez un prénom")
    @NotBlank(groups = {OnUpdate.class}, message = "Indiquez un prénom")
    private String firstName;
    @NotBlank(message = "Indiquez un numéro de téléphone")
    @NotBlank(groups = {OnUpdate.class},message = "Indiquez un numéro de téléphone")
    private String phone;
    @NotNull(message = "Sélectionnez un role")
    @NotNull(groups = {OnUpdate.class}, message = "Sélectionnez un role")
    private Role role;
    private boolean admin;
}
