package com.myhomeledger.app.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank
    @Size(min = 10, max = 10)
    private String phoneNumber;

    @NotBlank
    @Size(min = 8, max = 128)
    private String password;
}
