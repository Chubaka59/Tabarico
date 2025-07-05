package com.gtarp.tabarico.config;

import com.gtarp.tabarico.exception.UserNotFoundException;
import com.gtarp.tabarico.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username)
                .map(user -> new User(user.getUsername(), user.getPassword(), getAuthorities(user)))
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private Set<GrantedAuthority> getAuthorities(com.gtarp.tabarico.entities.User user){
        if (user.getAdmin() || user.getRole().getName().equals("Responsable") || user.getRole().getName().equals("Milice")) {
            Set<GrantedAuthority> authorities = new HashSet<>();
            if (user.getAdmin()) {
                authorities.add(new SimpleGrantedAuthority("ADMIN"));
            }
            if (user.getRole().getName().equals("Responsable")) {
                authorities.add(new SimpleGrantedAuthority("RESPONSABLE"));
            }
            if (user.getRole().getName().equals("Milice")) {
                authorities.add(new SimpleGrantedAuthority("MILICE"));
            }
            return authorities;
        }
        return Set.of();
    }
}
