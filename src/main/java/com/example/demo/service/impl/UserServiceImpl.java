package com.example.demo.service.impl;

import com.example.demo.cache.CacheStore;
import com.example.demo.domain.RequestContext;
import com.example.demo.entity.ConfirmationEntity;
import com.example.demo.entity.CredentialEntity;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enumeration.Authority;
import com.example.demo.enumeration.EventType;
import com.example.demo.enumeration.LoginType;
import com.example.demo.event.UserEvent;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.ConfirmationRepository;
import com.example.demo.repository.CredentialRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.google.common.cache.Cache;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

import static com.example.demo.utils.UserUtils.createUserEntity;


@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl  implements UserService {
    private final UserRepository userRepository;
    private  final RoleRepository roleRepository;
    private final CredentialRepository credentialRepository;
    private final ConfirmationRepository confirmationRepository;

//    private  final BCryptPasswordEncoder encoder;
    private final CacheStore<String, Integer> userCache;
    private final ApplicationEventPublisher publisher;
    @Override
    public void createUser(String firstName, String lastName, String email, String password) {
        var userEntity = userRepository.save(createNewUser(firstName,lastName,email));
        var credentialEntity = new CredentialEntity(userEntity,password);
        credentialRepository.save(credentialEntity);
        var confirmationEntity = new ConfirmationEntity(userEntity);
        confirmationRepository.save(confirmationEntity);
        publisher.publishEvent(new UserEvent(userEntity, EventType.REGISTRATION, Map.of("key", confirmationEntity.getKey())));
    }

    @Override
    public RoleEntity getRoleName(String name) {
        var role = roleRepository.findByNameIgnoreCase(name);
        return role.orElseThrow( () -> new ApiException("Role not Found"));
    }

    @Override
    public void verifyAccountKey(String key){
        var confirmationEntity = getUserConfirmation(key);
        UserEntity userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        confirmationRepository.delete(confirmationEntity);
    }

    @Override
    public void updateLoginAttempt(String email, LoginType loginType) {
        var userEntity = getUserEntityByEmail(email);
        RequestContext.setUserId(userEntity.getId());
        switch (loginType){
            case LOGIN_ATTEMPT -> {
                if(userCache.get(userEntity.getEmail()) == null){
                    userEntity.setLoginAttempts(0);
                    userEntity.setAccountNonLocked(true);
                }
                userEntity.setLoginAttempts(userEntity.getLoginAttempts()+1);
                userCache.put(userEntity.getEmail(), userEntity.getLoginAttempts());

                if(userCache.get(userEntity.getEmail()) > 5){
                    userEntity.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                userEntity.setAccountNonExpired(true);
                userEntity.setLoginAttempts(0);
                userEntity.setLastLogin(LocalDateTime.now());
                userCache.evict(userEntity.getEmail());
            }
        }
        userRepository.save(userEntity);
    }


    private UserEntity getUserEntityByEmail (String email){
        var userByEmail = userRepository.findByEmailIgnoreCase(email);
        return userByEmail.orElseThrow(() -> new ApiException("User not found"));
    }

    private ConfirmationEntity getUserConfirmation(String key) {
        return confirmationRepository.findByKey(key).orElseThrow(() -> new ApiException("Confirmation key  not found"));
    }

    private UserEntity createNewUser(String firstName, String lastName, String email){
        var role = getRoleName(Authority.USER.name());
        return createUserEntity(firstName,lastName,email,role);
    }






}
