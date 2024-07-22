package com.example.demo.service;

import com.example.demo.entity.RoleEntity;
import com.example.demo.enumeration.Authority;

public interface UserService {
    void createUser(String firstName, String lastName, String email, String password);
    RoleEntity getRoleName( String name);
}
