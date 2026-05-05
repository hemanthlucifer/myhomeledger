package com.myhomeledger.app.user.controller;

import com.myhomeledger.app.user.dto.CreateUserDTO;
import com.myhomeledger.app.user.dto.UpdateUserDTO;
import com.myhomeledger.app.user.service.CreateUserService;
import com.myhomeledger.app.user.service.UpdateUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final CreateUserService createUserService;
    private final UpdateUserService updateUserServie;

    @PostMapping("/signup")
    public ResponseEntity<Object> userSignup(@RequestBody @Valid CreateUserDTO createUserDTO){
        createUserService.createUser(createUserDTO);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update")
    public ResponseEntity<Object> updateUser(@RequestParam String userId, @RequestBody @Valid UpdateUserDTO updateUserDTO){
        updateUserService.updateUser(userId,updateUserDTO.getUsername(),updateUserDTO.getPhoneNumber());
        return ResponseEntity.ok().build();
    }

}
