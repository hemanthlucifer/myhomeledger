package com.myhomeledger.app.user.service.impl;

import com.myhomeledger.app.user.dto.GetUserDTO;
import com.myhomeledger.app.user.exceptions.UserNotFoundException;
import com.myhomeledger.app.user.mapper.UserMapper;
import com.myhomeledger.app.user.repository.UserRepository;
import com.myhomeledger.app.user.service.GetUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GetUserServiceImpl implements GetUserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public GetUserDTO getUser(String userId) {
        return userRepository.findById(UUID.fromString(userId))
                .map(userMapper::toDTO).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }


}
