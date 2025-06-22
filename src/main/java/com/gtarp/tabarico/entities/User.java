package com.gtarp.tabarico.entities;

import com.gtarp.tabarico.dto.UserDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table
@NoArgsConstructor
@AllArgsConstructor
public class User {
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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(UserDto userDto) {
        this.username = userDto.getUsername();
        this.password = new BCryptPasswordEncoder().encode(userDto.getPassword());
        this.lastName = userDto.getLastName();
        this.firstName = userDto.getFirstName();
        this.phone = userDto.getPhone();
    }

    public User updatePassword(UserDto userDto) {
        this.password = new BCryptPasswordEncoder().encode(userDto.getPassword());
        return this;
    }
}
