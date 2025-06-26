package com.gtarp.tabarico.entities;

import com.gtarp.tabarico.dto.UserDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Data
@Table
@NoArgsConstructor
@AllArgsConstructor
public class User implements UpdatableEntity<User, UserDto> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String lastName;
    @NotBlank
    private String firstName;
    @NotBlank
    private String phone;
    private Boolean admin;
    @ManyToOne
    @JoinColumn(name = "role_id")
    @NotNull
    private Role role;

    public User(UserDto userDto) {
        this.username = userDto.getUsername();
        this.password = new BCryptPasswordEncoder().encode(userDto.getPassword());
        this.lastName = userDto.getLastName();
        this.firstName = userDto.getFirstName();
        this.phone = userDto.getPhone();
        this.role = userDto.getRole();
    }

    public User update(UserDto userDto) {
        this.username = userDto.getUsername();
        if(!userDto.getPassword().isBlank()) {
            this.password = new BCryptPasswordEncoder().encode(userDto.getPassword());
        }
        this.lastName = userDto.getLastName();
        this.firstName = userDto.getFirstName();
        this.phone = userDto.getPhone();
        this.role = userDto.getRole();
        return this;
    }

    public String toString() {
        return firstName + " " + lastName;
    }
}
