package com.example.demo.utils;

import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Empty;
import org.apache.catalina.User;

import java.util.UUID;

import static java.time.LocalDateTime.now;
import static org.apache.logging.log4j.util.Strings.EMPTY;

public class UserUtils {
    public static UserEntity createUserEntity(String firstName, String lastName, String email, RoleEntity role){
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .lastLogin(now())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .mfa(false)
                .enabled(false)
                .loginAttempts(0)
                .qrCodeSecret(EMPTY)
                .phone(EMPTY)
                .bio(EMPTY)
                .imageUrl("https://media.licdn.com/dms/image/C4E0BAQEkqxsxNnVAHw/company-logo_200_200/0/1660245000865/zero_gravity_cybernetics_logo?e=2147483647&v=beta&t=FrSGDan2nQFta1tRhbRvGJ5qs9P039AkfAYgDRQOIsM")
                .role(role)
                .build();
    }
}
