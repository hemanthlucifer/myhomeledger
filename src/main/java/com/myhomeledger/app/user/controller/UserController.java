package com.myhomeledger.app.user.controller;

import com.myhomeledger.app.user.dto.UpdateUserDTO;
import com.myhomeledger.app.user.service.UpdateUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UpdateUserService updateUserService;

    @PatchMapping("/update")
    public ResponseEntity<Object> updateUser(@RequestParam String userId, @RequestBody @Valid UpdateUserDTO updateUserDTO){
        updateUserService.updateUser(userId,updateUserDTO.getUsername(),updateUserDTO.getPhoneNumber());
        return ResponseEntity.ok().build();
    }

}
