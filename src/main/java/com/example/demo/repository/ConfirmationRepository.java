package com.example.demo.repository;

import com.example.demo.entity.ConfirmationEntity;
import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationRepository extends JpaRepository<ConfirmationEntity, Long> {
    Optional<ConfirmationEntity> findByKey(String key);
    Optional<ConfirmationEntity> findByUserEntity(UserEntity userEntity);
}
