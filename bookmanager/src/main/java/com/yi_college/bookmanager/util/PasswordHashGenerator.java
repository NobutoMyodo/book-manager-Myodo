package com.yi_college.bookmanager.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String adminPassword = encoder.encode("admin123");
        String userPassword = encoder.encode("user123");

        System.out.println("admin123 → " + adminPassword);
        System.out.println("user123  → " + userPassword);
    }
}
