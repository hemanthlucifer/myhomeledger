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
            UserEntity userEntity = userMapper.toEntity(createUserDTO);
            userRepository.save(userEntity);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(),e);
            throw new UserProcessException("Unable to create user with username: " + createUserDTO.getUsername() + " and phone number: " + createUserDTO.getPhoneNumber());
        }
    }

}
