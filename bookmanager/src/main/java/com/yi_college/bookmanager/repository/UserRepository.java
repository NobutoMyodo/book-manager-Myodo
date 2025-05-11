package com.yi_college.bookmanager.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.yi_college.bookmanager.entity.UserEntity;

@Mapper
public interface UserRepository {
    UserEntity findByUsername(String username);
    @Insert("INSERT INTO users (username, password, role, enabled) VALUES (#{username}, #{password}, #{role}, #{enabled})")
    void insert(UserEntity user);
}
