package com.example.demo.service.impl;

import com.example.demo.entity.ConfirmationEntity;
import com.example.demo.entity.CredentialEntity;
import com.example.demo.entity.RoleEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.enumeration.Authority;
import com.example.demo.enumeration.EventType;
import com.example.demo.event.UserEvent;
import com.example.demo.exception.ApiException;
import com.example.demo.repository.ConfirmationRepository;
import com.example.demo.repository.CredentialRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    private UserEntity createNewUser(String firstName, String lastName, String email){
        var role = getRoleName(Authority.USER.name());
        return createUserEntity(firstName,lastName,email,role);
    }






}
