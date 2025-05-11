package com.yi_college.bookmanager.command;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.yi_college.bookmanager.entity.UserEntity;
import com.yi_college.bookmanager.repository.UserRepository;

@Component
public class InitialUserSetup {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public InitialUserSetup(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createInitialUsers() {
        // 管理者ユーザー
        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole("ADMIN");
        admin.setEnabled(true);

        // 一般ユーザー
        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user123"));
        user.setRole("USER");
        user.setEnabled(true);

        // ユーザーをデータベースに保存
        userRepository.insert(admin);
        userRepository.insert(user);
    }
}
