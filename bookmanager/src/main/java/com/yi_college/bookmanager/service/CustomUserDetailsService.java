package com.yi_college.bookmanager.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yi_college.bookmanager.entity.UserEntity;
import com.yi_college.bookmanager.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        // プレフィックス ROLE_ を削除して役割を設定
        String role = userEntity.getRole();
        if (role.startsWith("ROLE_")) {
            role = role.substring(5); // ROLE_を削除
        }

        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(role) // 修正された役割名を設定
                .disabled(!userEntity.isEnabled())
                .build();
    }
}
