package com.myhomeledger.app.user.controller;

import com.myhomeledger.app.user.dto.UpdateUserDTO;
import com.myhomeledger.app.user.service.UpdateUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {

    private final UpdateUserService updateUserService;

    @PatchMapping("/update")
    public ResponseEntity<Object> updateUser(@RequestParam String userId, @RequestBody @Valid UpdateUserDTO updateUserDTO){
        log.info("Update user request received for user {}", userId);
        updateUserService.updateUser(userId,updateUserDTO.getUsername(),updateUserDTO.getPhoneNumber());
        log.info("Update user request completed for user {}", userId);
        return ResponseEntity.ok().build();
    }

}
