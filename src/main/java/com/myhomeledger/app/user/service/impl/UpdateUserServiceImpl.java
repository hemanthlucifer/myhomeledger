package com.myhomeledger.app.user.service.impl;

import com.myhomeledger.app.user.entity.UserEntity;
import com.myhomeledger.app.user.exceptions.UserNotFoundException;
import com.myhomeledger.app.user.repository.UserRepository;
import com.myhomeledger.app.user.service.UpdateUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class UpdateUserServiceImpl implements UpdateUserService {

    private final UserRepository userRepository;

    public UpdateUserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public void updateUser(String userId,String userName, String phoneNumber) {
        log.info("Updating user {}", userId);
        UserEntity existingUserEntity = userRepository.findById(java.util.UUID.fromString(userId))
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        checkAndUpdatePhoneNumber(phoneNumber, existingUserEntity);
        existingUserEntity.setUserName(userName);
        userRepository.save(existingUserEntity);
        log.info("Updated user {}", userId);
    }


    private void checkAndUpdatePhoneNumber(String newPhoneNumber, UserEntity existingUserEntity) {
        if (newPhoneNumber != null && !newPhoneNumber.equals(existingUserEntity.getPhoneNumber())) {
            if (userRepository.existsByPhoneNumberAndUserIdNot(newPhoneNumber, existingUserEntity.getUserId())) {
                log.warn("Phone number update blocked for user {}", existingUserEntity.getUserId());
                throw new IllegalArgumentException("Phone number already exists for another user.");
            }
            existingUserEntity.setPhoneNumber(newPhoneNumber);
        }
    }


}
