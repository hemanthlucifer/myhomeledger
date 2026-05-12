package com.myhomeledger.app.user.service.impl;

import com.myhomeledger.app.user.dto.CreateUserDTO;
import com.myhomeledger.app.user.entity.UserEntity;
import com.myhomeledger.app.user.exceptions.UserProcessException;
import com.myhomeledger.app.user.mapper.UserMapper;
import com.myhomeledger.app.user.repository.UserRepository;
import com.myhomeledger.app.user.service.CreateUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateUserServiceServiceImpl implements CreateUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    @Override
    public void createUser(CreateUserDTO createUserDTO) {
        try{
            log.info("Creating user");
            UserEntity userEntity = userMapper.toEntity(createUserDTO);
            userRepository.save(userEntity);
            log.info("Created user {}", userEntity.getUserId());
        } catch (Exception e) {
            log.error("Failed to create user", e);
            throw new UserProcessException("Unable to create user");
        }
    }

}
