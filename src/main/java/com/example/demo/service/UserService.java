package com.example.demo.service;

import com.example.demo.dto.User;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enumeration.Authority;
import com.example.demo.enumeration.LoginType;

public interface UserService {
    void createUser(String firstName, String lastName, String email, String password);
    RoleEntity getRoleName( String name);
    void verifyAccountKey(String key);
    void updateLoginAttempt(String email, LoginType loginType);
    UserEntity getUserByUserId(String userId);

}
