package com.yi_college.bookmanager.entity;

import lombok.Data;

@Data
public class UserEntity {
    private Integer id;
    private String username;
    private String password;
    private String role;
    private Boolean enabled;

    public boolean isEnabled() {
        return Boolean.TRUE.equals(enabled);
    }
}
