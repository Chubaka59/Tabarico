package com.gtarp.tabarico.dto;

import com.gtarp.tabarico.validation.PasswordModification;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    @NotBlank
    private String username;
    @NotBlank
    @NotBlank(groups = PasswordModification.class)
    private String password;
    @NotBlank
    private String lastName;
    @NotBlank
    private String firstName;
    @NotBlank
    private String phone;
}
